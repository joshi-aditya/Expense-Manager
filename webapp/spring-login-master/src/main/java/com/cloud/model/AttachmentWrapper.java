package com.cloud.model;

import java.util.List;

public class AttachmentWrapper extends Status {

	private List<Attachment> attachments;

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
}
