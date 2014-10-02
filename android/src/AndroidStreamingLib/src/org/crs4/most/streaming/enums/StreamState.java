package org.crs4.most.streaming.enums;

public enum StreamState {
	/**
	 * the stream has not been initialized yet , the initialization failed or it has been uninitialized
	 */
    UNINITIALIZED,  
    /**
	 * the stream was successfully initialized and it is ready to play
	 */
    INITIALIZED,
    
    /**
     * the stream is playing
     */
    PLAYING,
    
    /**
     * the stream is in pause state
     */
    PAUSED,
 
}
