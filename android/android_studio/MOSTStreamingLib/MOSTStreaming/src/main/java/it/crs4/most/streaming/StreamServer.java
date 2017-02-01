package it.crs4.most.streaming;


public interface StreamServer {
    public void start();
    public void stop();
    public void feedData(byte [] data);
}
