package org.crs4.most.streaming.examples;



import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class StreamFragment extends Fragment {
	 
	 public static final String FRAGMENT_STREAM_ID_KEY = "stream_fragment_stream_id_key";
	 
	 private IStreamFragmentCommandListener cmdListener = null;
	 private SurfaceView surfaceView = null;
	 
	 public static  StreamFragment newInstance(String streamId) {
		 StreamFragment sf = new StreamFragment();

	        Bundle args = new Bundle();
	        args.putString(FRAGMENT_STREAM_ID_KEY, streamId);
	        sf.setArguments(args);

	        return sf;
	    }
	 
	 private String getStreamId()
	 {
		 return getArguments().getString(FRAGMENT_STREAM_ID_KEY);
	 }
	
	 @Override
	 public void onCreate(Bundle savedInstanceState)
	 {
		 super.onCreate(savedInstanceState);
	 }
	 
	@Override 
	public void onActivityCreated(Bundle bundle){
		super.onActivityCreated(bundle);
		StreamFragment.this.cmdListener.onSurfaceViewCreated(getStreamId(),this.surfaceView);
	}
	 
	    
	   @Override
	   public void onAttach(Activity activity) {
		   super.onAttach(activity);
		   this.cmdListener = (IStreamFragmentCommandListener) activity;
	   }
	   
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState)
	          {
		        View rootView = inflater.inflate(R.layout.stream_layout, container, false);
		        
		        this.surfaceView = (SurfaceView) rootView.findViewById(R.id.streamSurface);
		        
		        ImageButton butPlay = (ImageButton)  rootView.findViewById(R.id.button_play);
		        butPlay.setOnClickListener(new OnClickListener() {
		            public void onClick(View v) {
		            	StreamFragment.this.cmdListener.onPlay(getStreamId());
	            }
	        });
	        
	        
	        ImageButton butPause = (ImageButton)  rootView.findViewById(R.id.button_pause);
	        butPause.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	StreamFragment.this.cmdListener.onPause(getStreamId());
	            }
	        });
	        
	     
			
            
	        return rootView;
	          }

		public SurfaceView getSurfaceView() {
			// TODO Auto-generated method stub
			return this.surfaceView;
		}
}
