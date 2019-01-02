package stats;

public final class UserChatStats {

	private String gmail;
	private String sender;
	private int numMessages;
	private String videoId;

	public UserChatStats(final String gmail, final String sender, 
			final int numMessages, final String videoId) {
		this.gmail = gmail;
		this.sender = sender;
		this.numMessages = numMessages;
		this.videoId = videoId;
	}

	public String getGmail() {
		return gmail;
	}

	public String getSender() {
		return sender;
	}

	public int getNumMessages() {
		return numMessages;
	}
	
	public String getVideoId() {
		return this.videoId;
	}

}
