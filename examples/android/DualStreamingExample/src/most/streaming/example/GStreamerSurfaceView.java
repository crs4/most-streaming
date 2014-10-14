/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package most.streaming.example;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

// A simple SurfaceView whose width and height can be set from the outside
public class GStreamerSurfaceView extends SurfaceView {
    public int media_width = 320;
    public int media_height = 240;
    
    private static String TAG = "GStreamerSurfaceView";
    
    // Mandatory constructors, they do not do much
    public GStreamerSurfaceView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public GStreamerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GStreamerSurfaceView (Context context) {
        super(context);
    }

    
    
    // Called by the layout manager to find out our size and give us some rules.
    // We will try to maximize our size, and preserve the media's aspect ratio if
    // we are given the freedom to do so.
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0, height = 0;
        int wmode = View.MeasureSpec.getMode(widthMeasureSpec);
        int hmode = View.MeasureSpec.getMode(heightMeasureSpec);
        int wsize = View.MeasureSpec.getSize(widthMeasureSpec);
        int hsize = View.MeasureSpec.getSize(heightMeasureSpec);

        Log.d (TAG, "onMeasure called with size w::" +  wsize  + "h::" + hsize);
        Log.d (TAG, "onMeasure called with media_width::" +  media_width  + " media_height::" + media_height);
        // Obey width rules
        switch (wmode) {
        case View.MeasureSpec.AT_MOST:
            if (hmode == View.MeasureSpec.EXACTLY) {
                width = Math.min(hsize * media_width / media_height, wsize);
                break;
            }
        case View.MeasureSpec.EXACTLY:
            width = wsize;
            break;
        case View.MeasureSpec.UNSPECIFIED:
            width = media_width;
        }

        // Obey height rules
        switch (hmode) {
        case View.MeasureSpec.AT_MOST:
            if (wmode == View.MeasureSpec.EXACTLY) {
                height = Math.min(wsize * media_height / media_width, hsize);
                break;
            }
        case View.MeasureSpec.EXACTLY:
            height = hsize;
            break;
        case View.MeasureSpec.UNSPECIFIED:
            height = media_height;
        }

        // Finally, calculate best size when both axis are free
        if (hmode == View.MeasureSpec.AT_MOST && wmode == View.MeasureSpec.AT_MOST) {
            int correct_height = width * media_height / media_width;
            int correct_width = height * media_width / media_height;

            if (correct_height < height)
                height = correct_height;
            else
                width = correct_width;
        }

        // Obey minimum size
        width = Math.max (getSuggestedMinimumWidth(), width);
        height = Math.max (getSuggestedMinimumHeight(), height);
        Log.d (TAG, "Setting size: " + width + "," + height);
        setMeasuredDimension(width, height);
    }
    
    /* Another sizing policy... choose the policy you prefer or implement a new one...
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	
    	int desiredWidth = this.media_width;
    	int desiredHeight = this.media_height;

    	int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    	int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    	int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    	int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    	int width;
    	int height;

    	//Measure Width
    	if (widthMode == MeasureSpec.EXACTLY) {
    		Log.d(TAG,"onMeasure width spec EXACTLY");
    	    //Must be this size
    	    width = widthSize;
    	} else if (widthMode == MeasureSpec.AT_MOST) {
    		Log.d(TAG,"onMeasure width spec AT_MOST");
    	    //Can't be bigger than...
    	    width = Math.min(desiredWidth, widthSize);
    	} else {
    		Log.d(TAG,"onMeasure with spec UNSPECIFIED");
    	    width = desiredWidth;
    	}

    	//Measure Height
    	if (heightMode == MeasureSpec.EXACTLY) {
    		Log.d(TAG,"onMeasure height spec EXACTLY");
    	    //Must be this size
    	    height = heightSize;
    	} else if (heightMode == MeasureSpec.AT_MOST) {
    		Log.d(TAG,"onMeasure height spec AT_MOST");
    	    //Can't be bigger than...
    	    height = Math.min(desiredHeight, heightSize);
    	} else {
    		Log.d(TAG,"onMeasure height spec UNSPECIFIED");
    	    //Be whatever you want
    	    height = desiredHeight;
    	}

    	//MUST CALL THIS
    	Log.d (TAG, "Setting video size on surface: " + width + "," + height);
    	setMeasuredDimension(width, height);
    }
    */
    
}