package model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage {

	private String videoId;
	private String sender;
	private String message;
	private Date time;
	private String googleUserId;
	private String gmail;
	private String type;
	private String title;
	private String description;
	private String profileImageUrl;
	private String liveChatId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGoogleUserId() {
		return googleUserId;
	}

	public void setGoogleUserId(String googleUserId) {
		this.googleUserId = googleUserId;
	}

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getLiveChatId() {
		return liveChatId;
	}

	public void setLiveChatId(String liveChatId) {
		this.liveChatId = liveChatId;
	}

	@Override
	public String toString() {
		return "ChatMessage [videoId=" + videoId + ", sender=" + sender + ", message=" + message + ", time=" + time
				+ ", googleUserId=" + googleUserId + ", gmail=" + gmail + ", type=" + type + ", title=" + title
				+ ", description=" + description + ", profileImageUrl=" + profileImageUrl + ", liveChatId=" + liveChatId
				+ "]";
	}
}
