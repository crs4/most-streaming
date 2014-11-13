package org.crs4.most.streaming.ptz;

import java.util.HashMap;
import java.util.Map;

import org.crs4.most.streaming.enums.PTZ_Direction;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PTZ_Manager {

	private String uri;
	private String username;
	private Context ctx;
	
	private static String TAG = "PTZ_Manager";
	
	public PTZ_Manager(Context ctx, String uri, String username, String password)
	{
		this.ctx=ctx;
		this.uri = uri;
		this.username = username;
		this.password = password;
	}
	
	
	public void startMove(PTZ_Direction direction)
	{
		this.move(direction,30);
	}
	
	public void stopMove()
	{
		this.move(PTZ_Direction.STOP ,0);
	}
	
	public void move(PTZ_Direction direction, int speed)
	{
		//self.do_cmd('continuouspantiltmove=%s,%s' % ((direction[0]*speed), (direction[1]*speed)))
		String cmd = "continuouspantiltmove=" + String.valueOf(direction.getX()*speed) + "," + String.valueOf(direction.getY()*speed);
		this.sendPtzCmd(cmd);
	}
	
	 private void sendPtzCmd(String cmd)
     {
     	//String url = "http://192.168.1.80:8000/crib/snow_on/";
     	String url =  this.uri + "?" + cmd;
        
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
            	 Log.d(TAG,"That didn't work:" + error.getMessage());
             }


         }) {
        	 /*
             @Override
             protected Map<String, String> getParams() 

             {  

                     Map<String, String>  params = new HashMap<String, String>();  

                             params.put("username", "specialista");  

                             params.put("password", "speciali");

                             //params.put("client_id", "d72835cfb6120e844e13");

                             //params.put("client_secret", "8740cac9a53f2cdd1bded9cfbb60fdb3b5396863");

                            // params.put("grant_type", "password");

                      

                     return params;  

             }
             */

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

	private String password;

}
