package com.paicbd.smsc.ws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.stomp.StompSession;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class SocketSessionTest {

    @Mock
    StompSession stompSession;

    SocketSession socketSession;

    @BeforeEach
    void setUp() {
        socketSession = new SocketSession("gw");
    }

    @Test
    void sendStatusTest() {
        // Successfully
        socketSession.setStompSession(stompSession);
        assertDoesNotThrow(() -> socketSession.sendStatus("systemId", "param", "value"));

        // Null StompSession
        socketSession.setStompSession(null);
        assertDoesNotThrow(() -> socketSession.sendStatus("systemId", "param", "value"));
    }
}