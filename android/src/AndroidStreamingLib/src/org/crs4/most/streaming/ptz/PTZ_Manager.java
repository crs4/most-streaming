package org.crs4.most.streaming.ptz;

import java.util.HashMap;
import java.util.Map;

import org.crs4.most.streaming.enums.PTZ_Direction;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PTZ_Manager {

	private String uri;
	private String username;
	private String password;
	private Context ctx;
	
	private static String TAG = "PTZ_Manager";
	
	/**
	 * Handles ptz commands of a remote Axis webcam.
	 * @param ctx The activity context
	 * @param uri The ptz uri of the webcam 
	 * @param username the username used for ptz authentication
	 * @param password  the username used for ptz authentication
	 */
	public PTZ_Manager(Context ctx, String uri, String username, String password)
	{
		this.ctx=ctx;
		this.uri = uri;
		this.username = username;
		this.password = password;
	}
	
	
	/**
	 * Command for start moving the webcam in a specified direction
	 *  
	 * @param direction the direction {@link PTZ_Direction.STOP}} stops the webcam
	 */
	public void startMove(PTZ_Direction direction)
	{
		this.startMove(direction,30);
	}
	
	public void stopMove()
	{
		this.startMove(PTZ_Direction.STOP ,0);
	}
	
	public void startMove(PTZ_Direction direction, int speed)
	{
		String cmd =  String.format("continuouspantiltmove=%s,%s",String.valueOf(direction.getX()*speed),String.valueOf(direction.getY()*speed));  
		this.sendPtzCmd(cmd);
	}
	
	 private void sendPtzCmd(String cmd)
     {
     	 String url =  String.format("%s?%s",this.uri, cmd); 
  
         Log.d(TAG, "sending command:"+ cmd);
         
         // Instantiate the RequestQueue.
         RequestQueue queue = Volley.newRequestQueue(this.ctx);

         // Request a string response from the provided URL.
         @SuppressWarnings({ "rawtypes", "unchecked" })
			StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                     new Response.Listener<String>() {
         	   
				
				@Override
				public void onResponse(String response) {
					Log.d(TAG,"Response is: "+  response);
				}
				
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
            	 Log.d(TAG,"Http Response Error:" + error.getMessage());
             }


         }) {
        	 	@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
				    HashMap<String, String> params = new HashMap<String, String>();
				    String creds = String.format("%s:%s",username,password);
				    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
				    params.put("Authorization", auth);
				    return params;
				}
         };
         
         
         // Add the request to the RequestQueue.
         queue.add(stringRequest);

     }
	public String getUri() {
		return uri;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	

}
