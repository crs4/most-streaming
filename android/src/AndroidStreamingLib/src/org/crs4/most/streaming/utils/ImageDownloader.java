package org.crs4.most.streaming.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;


import com.android.volley.AuthFailureError;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class ImageDownloader {
	
	public interface IBitmapReceiver {
		
		public void onBitmapDownloaded(ImageDownloader imageDownloader, Bitmap image);
		public void onBitmapDownloadingError(ImageDownloader imageDownloader, Exception ex);
		public void onBitmapSaved(ImageDownloader imageDownloader, String filename);
		public void onBitmapSavingError(ImageDownloader imageDownloader, Exception ex);
	}
	
	private String username;
	private String password;
    private Context ctx;
    
    private static final String TAG = "ImageDownloader";
    
    IBitmapReceiver receiver = null;
    
    
    /**
     * This class handles asynchronous image downloads.For each downloaded image, call the method {@link ImageDownloader.IBitmapReceiver#onBitmapReady(Bitmap)}}
     * @param receiver the object that will receive the downloaded image
     * @param ctx the activity context
     * @param username the user name (needed for authentication)
     * @param password the password (needed for authentication)
     */
	public ImageDownloader(IBitmapReceiver receiver, Context ctx, String username, String password)
	{
		this.username = username;
		this.password = password;
		this.ctx = ctx;
		this.receiver = receiver;
	}
	
	public void downloadImage(String url)
    {     
		ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {
			
		    @Override
		    public void onResponse(Bitmap response) {
		    	receiver.onBitmapDownloaded(ImageDownloader.this, response);
		    }
		    
		}, 0, 0, null, null) {
			
	
		    @Override
			public Map<String, String> getHeaders() throws AuthFailureError {
		    	  HashMap<String, String> params = new HashMap<String, String>();
		    	  String creds = String.format("%s:%s",username,password);
		    	  String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
		    	  params.put("Authorization", auth);
		    	  return params;
		      }
		};
		
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.ctx);

        // Add the request to the RequestQueue.
        queue.add(ir);

    }
    
	public void logAppFileNames()
	{
		String [] files = ctx.getFilesDir().list();
		for (String fn : files)
		{
			Log.d("TAG", "File:" + fn);
		}
	}
	
	/**
	 * Method to save bitmap into internal memory
	 * @param image and context
	 */
		public void saveImageToInternalStorage(Bitmap image,String filename)
		{
		try {
			FileOutputStream fos = ctx.openFileOutput(filename+ ".jpg", Context.MODE_PRIVATE);
			image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			                // 100 means no compression, the lower you go, the stronger the compression
			fos.close();
			Log.d(TAG, "Image saved on dir:" + ctx.getFilesDir().getAbsolutePath() );
			receiver.onBitmapSaved(this, filename);
		}
		catch (Exception e) {
			Log.e("saveToInternalStorage()", e.getMessage());
			receiver.onBitmapDownloadingError(this, e);
				}
		}
   
		
		public static File [] getInternalImages(Context ctx)
		{
			File appDir = ctx.getFilesDir();
			File[] jpgFiles = appDir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					 
					return pathname.exists() && pathname.getAbsolutePath().endsWith(".jpg");
				}});
			
			
			return  jpgFiles;
		}
		
		public void loadImageFromInternalStorage(String filename) {

			Bitmap thumbnail = null;

			
			try {
				File filePath = ctx.getFileStreamPath(filename);
				FileInputStream fi = new FileInputStream(filePath);
				thumbnail = BitmapFactory.decodeStream(fi);
				
			
				} catch (Exception ex) {
						Log.e( "getThumbnail() on internal storage", ex.getMessage());
						receiver.onBitmapDownloadingError(this, ex);
						return;
				}
			receiver.onBitmapDownloaded(ImageDownloader.this, thumbnail);
			}
}
