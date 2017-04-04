package it.crs4.most.streaming;


public interface StreamServer {
    public void start(int videoWidth, int videoHeight);
    public void stop();
    public void feedData(byte [] data);
}
