package com.paicbd.smsc.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WatcherTest {

    @Test
    void testInstanceCreation() {
        assertDoesNotThrow(() -> new Watcher("TEST-WATCHER", new AtomicInteger(27), 1));
    }

    @Test
    void testStartWatching() throws InterruptedException {
        Watcher watcher = new Watcher("TEST-WATCHER", new AtomicInteger(34), 1);
        Thread.sleep(2000);
        assertDoesNotThrow(watcher::startWatching);
    }

    @Test
    void testStopWatching() {
        Watcher watcher = new Watcher("TEST-WATCHER", new AtomicInteger(0), 1);
        assertDoesNotThrow(watcher::stopWatching);
    }
}