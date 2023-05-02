package com.radu.dlx.io.progress;

import java.util.ArrayList;
import java.util.List;

public class TeeTrackingStream implements TrackingStream {
    private final List<StreamListener> listeners = new ArrayList<>();

    public TeeTrackingStream() {
    }

    public void register(StreamListener listener) {
        listeners.add(listener);
    }

    public void write(PrintableEvent event) {
        for (StreamListener listener : listeners) {
            listener.consume(event);
        }
    }

}
