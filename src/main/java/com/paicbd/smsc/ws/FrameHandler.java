package com.paicbd.smsc.ws;

import org.springframework.messaging.simp.stomp.StompHeaders;

// This interface must be implemented by all modules with connection to websocket server
public interface FrameHandler {
    void handleFrameLogic(StompHeaders headers, Object payload);
}
