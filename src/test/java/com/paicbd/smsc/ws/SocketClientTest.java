package com.paicbd.smsc.ws;

import com.paicbd.smsc.dto.UtilsRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SocketClientTest {

    @Mock
    FrameHandler frameHandler;

    @Mock
    SocketSession socketSession;

    @Mock
    StompSession stompSession;

    UtilsRecords.WebSocketConnectionParams webSocketConnectionParams;
    SocketClient socketClient;

    @BeforeEach
    void setUp() {
        webSocketConnectionParams = new UtilsRecords.WebSocketConnectionParams(
                true,
                "localhost",
                5082,
                "/ws",
                List.of("topic01", "topic02"),
                "Authorization",
                "1234567890",
                10000,
                "SMSC-UTILS-TEST"
        );

        socketClient = new SocketClient(frameHandler, webSocketConnectionParams, socketSession);
    }

    @Test
    void afterConnectedTest() {
        StompHeaders headers = new StompHeaders();
        headers.setDestination("topic01");
        assertDoesNotThrow(() -> socketClient.afterConnected(stompSession, headers));
        assertThrows(Exception.class, () -> socketClient.afterConnected(stompSession, null));
    }

    @Test
    void handleExceptionTest() {
        StompHeaders headers = new StompHeaders();
        headers.setDestination("topic01");
        StompCommand command = StompCommand.CONNECTED;
        byte[] payload = "Test Payload".getBytes();
        Throwable exception = new Exception("Test Exception");
        assertDoesNotThrow(() -> socketClient.handleException(stompSession, command, headers, payload, exception));

        assertThrows(Exception.class, () -> socketClient.handleException(null, command, headers, payload, exception));
        assertThrows(Exception.class, () -> socketClient.handleException(stompSession, command, null, payload, exception));
        assertThrows(Exception.class, () -> socketClient.handleException(stompSession, command, headers, payload, null));
    }

    @Test
    void getPayloadTypeTest() {
        StompHeaders headers = new StompHeaders();
        headers.setDestination("topic01");
        assertDoesNotThrow(() -> socketClient.getPayloadType(headers));
    }

    @Test
    void handleFrameTest() {
        StompHeaders headers = new StompHeaders();
        headers.setDestination("topic01");
        assertDoesNotThrow(() -> socketClient.handleFrame(headers, "Test Payload"));
        assertDoesNotThrow(() -> socketClient.handleFrameLogic(headers, "Test Payload"));
        assertThrows(Exception.class, () -> socketClient.handleFrame(null, "all"));
    }

    @Test
    void handleTransportErrorTest() {
        assertDoesNotThrow(() -> socketClient.handleTransportError(stompSession, new Exception("Test Exception")));
        assertThrows(Exception.class, () -> socketClient.handleTransportError(null, new Exception("Test Exception")));
        assertThrows(Exception.class, () -> socketClient.handleTransportError(stompSession, null));
    }
}