package org.crs4.most.streaming;

import java.util.HashMap;

import android.content.Context;
import android.os.Handler;


/**
 * This class provide factory methods for creating {@link IStream} objects.
 *
 *
 */
public class StreamingFactory {
	
	/**
	 * 
	 * @return a new IStream instance
	 */
	/**
	 * This factory method provides a new IStrean instance
	 * @param context the application context
	 * @param configParams All needed configuration string parameters. All the supported parameters are the following:
	 * 	<ul>
	 * 		<li>name: (mandatory) the name of the stream (it must be unique for stream)</li>
	 * 		<li>uri: (mandatory) the uri of the stream (it can be also changed later)</li>
	 * 		<li>latency: (optional) the preferred latency of the stream in ms (default value: 200 ms) </li>
	 * 	</ul>
	 * 
	 * @param notificationHandler the handler where to receive all notifications from the Library
	 *
	 * @throws Exception if an error occurred during the stream initialization
	 */
	public static IStream getIStream(Context context, HashMap<String,String> configParams, Handler notificationHandler) throws Exception
	{
		return new GStreamerBackend(context,configParams, notificationHandler);
	}

}
