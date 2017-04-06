package it.crs4.most.streaming;


public interface StreamServer {
    void start(int videoWidth, int videoHeight, int rate);
    void stop();
    void feedData(byte [] data);
    boolean isRunning();
}
