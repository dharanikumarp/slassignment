package stats;

public final class ChatStats {

	private String videoId;
	private int numMessages;

	public ChatStats(final String videoId, int numMessages) {
		this.videoId = videoId;
		this.numMessages = numMessages;
	}

	public String getVideoId() {
		return videoId;
	}

	public int getNumMessages() {
		return numMessages;
	}
}
