package com.cloud.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Status {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String statusCode;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
