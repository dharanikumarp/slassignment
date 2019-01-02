package stats;

import java.util.ArrayList;
import java.util.List;

public final class RollingTimeChatStats {
	private String videoId;
	private List<Long> rollingSummary = new ArrayList<>();

	public RollingTimeChatStats(final String videoId, long m1, long m5, long m10, long m30, long minute) {
		this.videoId = videoId;
		rollingSummary.add(m1);
		rollingSummary.add(m5);
		rollingSummary.add(m10);
		rollingSummary.add(m30);
		rollingSummary.add(minute);
	}

	public String getVideoId() {
		return this.videoId;
	}

	public List<Long> getRollingSummary() {
		return this.rollingSummary;
	}
}
