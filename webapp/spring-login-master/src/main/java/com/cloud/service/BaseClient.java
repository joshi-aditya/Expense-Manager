package com.cloud.service;

import org.springframework.web.multipart.MultipartFile;

public interface BaseClient {

	public String uploadFile(MultipartFile multipartFile, String userId) throws Exception;
	
	public String deleteFile(String fileUrl) throws Exception;
	
}
