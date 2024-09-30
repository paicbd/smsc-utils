package com.paicbd.smsc.ws;

import com.paicbd.smsc.dto.UtilsRecords;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class SocketClient extends StompSessionHandlerAdapter implements FrameHandler {
    private final FrameHandler frameHandler;
    private final SocketSession socketSession;
    private final UtilsRecords.WebSocketConnectionParams params;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public SocketClient(FrameHandler frameHandler, UtilsRecords.WebSocketConnectionParams params, SocketSession socketSession) {
        log.warn("Initializing websocket with params: {}", params);
        this.frameHandler = frameHandler;
        this.params = params;
        this.socketSession = socketSession;
        if (params.isWsEnabled()) {
            this.connectWebSocket();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void connectWebSocket() {
        log.info("Connecting to websocket");
        WebSocketStompClient stompClient =
                new WebSocketStompClient(new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add(this.params.headerKey(), this.params.headerValue());
        String url = "ws://" + this.params.host() + ":" + this.params.port() + this.params.path();
        stompClient.connectAsync(url, headers, this);
    }

    @Override
    public void afterConnected(StompSession session, @NonNull StompHeaders connectedHeaders) {
        log.info("WebSocket Connected:{}", session.getSessionId());
        this.params.topicsToSubscribe().forEach(topic -> session.subscribe(topic, this));
        session.send("/app/session-confirm", this.params.module() + ":" + session.getSessionId());
        this.socketSession.setStompSession(session);
    }

    @Override
    public void handleException(
            @NonNull StompSession session, StompCommand command,
            @NonNull StompHeaders headers, @Nonnull byte[] payload,
            @NonNull Throwable exception) {
        this.scheduleRetry();
        log.error("Exception occurred", exception);
    }

    @Override
    public void handleTransportError(@NonNull StompSession session, @NonNull Throwable exception) {
        this.scheduleRetry();
        log.error("An error occurred while communicating with the websocket server");
    }

    @Override
    @NonNull
    public Type getPayloadType(@Nonnull StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(@NonNull StompHeaders headers, Object payload) {
        this.frameHandler.handleFrameLogic(headers, payload);
    }

    @Override
    public void handleFrameLogic(StompHeaders headers, Object payload) {
        log.info("From websocket server: {}", payload);
    }

    public void scheduleRetry() {
        int delaySeconds = this.params.retryInterval();
        log.warn("Scheduling retry connection to websocket in {} seconds", delaySeconds);
        executorService.schedule(this::connectWebSocket, delaySeconds, TimeUnit.SECONDS);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
