package org.crs4.most.streaming;


/**
 * This class provide factory methods for creating {@link IStream} objects.
 *
 *
 */
public class StreamingFactory {
	
	/**
	 * This factory method provides a new IStrean instance
	 * @return a new IStream instance
	 */
	public static IStream getIStream()
	{
		return new GStreamerBackend();
	}

}
