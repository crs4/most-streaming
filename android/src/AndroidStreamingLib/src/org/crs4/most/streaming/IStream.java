package org.crs4.most.streaming;


public interface IStream {
	
	 
    /**
     * 
     * @return the name of this stream
     */
    public String getName();
    
    
    /**
     * Prepares the stream (remote class loading, pipeline building, etc...)
     */
	public void prepare() throws Exception;
	
    /**
     * Play the stream
     */
	public void play() ;
	
	/**
	 * pause the stream
	 */
	public void pause();
	
	/**
	 * Update the uri of the stream
	 * @param uri the new uri
	 */
	public void setUri(String uri);
	
	/**
     * Get the current value of latency property of this stream (Reads the value from native code to be sure to return the effective latency value)
     * @return the latency value in ms
     */
	public int getLatency();
	
	/**
	 * Destroy this stream
	 */
	public void destroy();
}

