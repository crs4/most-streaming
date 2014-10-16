package org.crs4.most.streaming.examples;

import android.view.SurfaceView;

public interface IStreamFragmentCommandListener {

	public void onPlay(String streamId);
	public void onPause(String streamId);
	public void onSurfaceViewCreated(String streamId, SurfaceView surfaceView);
}
