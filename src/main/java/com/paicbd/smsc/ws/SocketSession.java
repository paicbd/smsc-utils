package com.paicbd.smsc.ws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompSession;

@Setter
@Getter
@Slf4j
@RequiredArgsConstructor
public class SocketSession {
    private StompSession stompSession;
    private final Object lock = new Object(); // the function of this lock is to prevent multiple threads from sending messages at the same time
    private final String type;

    private String message(String systemId, String param, String value) {
        return String.format("%s,%s,%s,%s", "gw", systemId, param, value);
    }

    public void sendStatus(String systemId, String param, String value) {
        if (stompSession != null) {
            synchronized (lock) {
                String message = this.message(systemId, param, value);
                log.info("/app/handler-status" + " -> {}", message);
                stompSession.send("/app/handler-status", message);
            }
        }
    }
}
