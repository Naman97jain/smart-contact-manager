package com.smartcontact.manager.helper;

public class Message {
	private String content;
	private String type;

	public Message(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
