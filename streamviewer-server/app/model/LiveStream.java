package model;

import java.util.Map;

public class LiveStream {

	private String videoId;
	private String channelId;
	private String title;
	private String description;
	private String liveChatId;
	private String channelTitle;
	private Map<String, Thumbnail> thumbnails;

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	public Map<String, Thumbnail> getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(Map<String, Thumbnail> thumbnails) {
		this.thumbnails = thumbnails;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLiveChatId() {
		return liveChatId;
	}

	public void setLiveChatId(String liveChatId) {
		this.liveChatId = liveChatId;
	}

	@Override
	public String toString() {
		return "LiveStream [videoId=" + videoId + ", channelId=" + channelId + ", title=" + title + ", description="
				+ description + ", liveChatId=" + liveChatId + ", channelTitle=" + channelTitle + ", thumbnails="
				+ thumbnails + "]";
	}
}
