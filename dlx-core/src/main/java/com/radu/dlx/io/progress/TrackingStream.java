package com.radu.dlx.io.progress;

//TODO: Should we send the tracking stream to a separate thread?
public interface TrackingStream {
    int DEFAULT_SOLUTION_STORE_MODULO = 1_000_000;//TODO: make configurable
    TrackingStream NULL_STREAM = new NullStream();
    void register(StreamListener listener);

    //TODO: does this have to be lazy? we can write immediately as well ...
    //TODO: case of premature optimization
    void write(PrintableEvent event);

    default boolean isWritable(int count) {
        return count == 0 || (count > DEFAULT_SOLUTION_STORE_MODULO && count % DEFAULT_SOLUTION_STORE_MODULO == 0);
    }

    class NullStream implements TrackingStream {

        @Override
        public void register(StreamListener listener) {

        }

        @Override
        public void write(PrintableEvent event) {

        }
    }
}
