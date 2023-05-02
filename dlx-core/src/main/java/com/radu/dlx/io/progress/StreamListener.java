package com.radu.dlx.io.progress;

@FunctionalInterface
public interface StreamListener {
    void consume(PrintableEvent event);
}
