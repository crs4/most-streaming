package org.crs4.most.streaming.examples;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

 
public class StreamDialogFragment extends DialogFragment {
	
	public static final String FRAGMENT_STREAM_ID_KEY = "stream_fragment_stream_id_key";
	public static final String FRAGMENT_STREAM_URI_KEY = "stream_fragment_stream_uri_key";
	public static final String FRAGMENT_STREAM_LATENCY_KEY = "stream_fragment_stream_uri_key";
	
	 public static  StreamDialogFragment newInstance(String streamId, String streamUri, int streamLatency) {
		 StreamDialogFragment sf = new StreamDialogFragment();

	        Bundle args = new Bundle();
	        args.putString(FRAGMENT_STREAM_ID_KEY, streamId);
	        args.putString(FRAGMENT_STREAM_URI_KEY, streamUri);
	        args.putInt(FRAGMENT_STREAM_LATENCY_KEY, streamLatency);
	        sf.setArguments(args);

	        return sf;
	    }
	 
	 private String getStreamId()
	 {
		 return getArguments().getString(FRAGMENT_STREAM_ID_KEY);
	 }
	 
	 private String getStreamUri()
	 {
		 return getArguments().getString(FRAGMENT_STREAM_URI_KEY);
	 }
	 
	 private int getStreamLatency()
	 {
		 return getArguments().getInt(FRAGMENT_STREAM_LATENCY_KEY);
	 }
	
	 /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface StreamDialogListener {
        public void onStreamDialogPositiveClick( StreamDialogFragment  dialog);
        public void onStreamDialogNegativeClick( StreamDialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    StreamDialogListener mListener;
    
    
 // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (StreamDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.istream_popup_editor, null));
         
        builder.setTitle(getStreamId());
        builder.setMessage("Prova")
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dismiss();
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dismiss();
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}