package org.crs4.most.streaming.enums;

public enum StreamState {
	/**
	 * the stream has not been initialized yet , the initialization failed or it has been deinitialized
	 */
    DEINITIALIZED,  
    
    /**
   	 * the stream has being initialized
   	 */
    INITIALIZING,
    
    /**
	 * the stream was successfully initialized and it is ready to play
	 */
    INITIALIZED,
    
    /**
     * a play request is sent to the stream that is preparing to start playing
     */
    PLAYING_REQUEST,
    
    
    /**
     * the stream is playing
     */
    PLAYING,
    
    /**
     * the stream is in pause state
     */
    PAUSED,
    
    /**
   	 * the stream has being deinitialized
   	 */
    DEINITIALIZING,
    
    /**
     * The stream is in an inconsistent state
     */
    ERROR
 
}
