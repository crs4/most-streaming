package org.crs4.most.streaming;


/**
 * This class provide factory methods to get an {@link IStream} object.
 *
 *
 */
public class StreamingFactory {
	
	public static IStream getIStream()
	{
		return new GStreamerBackend();
	}

}
