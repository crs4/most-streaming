package most.streaming.example;

public interface GStreamerListener {

	public void onGStreamerInitialized(GStreamerBackend gStreamerBackend);
	public void setMessage(GStreamerBackend gStreamerBackend,final String message);
	public void onMediaSizeChanged (GStreamerBackend gStreamerBackend, int width, int height);
}
