package com.cloud.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.constants.CommonConstants;
import com.cloud.util.Utils;

@Service
@Profile("default")
public class LocalClient implements BaseClient {

	private String localPath = ".//";

	@Override
	public String uploadFile(MultipartFile multipartFile) throws Exception {

		byte[] bytes = multipartFile.getBytes();
		Path path = Paths.get(localPath + Utils.generateFileName(multipartFile));
		Files.write(path, bytes);

		return path.toString();
	}

	@Override
	public String deleteFile(String fileUrl) throws Exception {

		File file = new File(fileUrl);
		String result = CommonConstants.DELETE_ATTACHMENTS_FAILURE;
		if(file.delete()){
			result = file.getName() + " " + CommonConstants.DELETE_ATTACHMENTS_SUCCESS;
		}

		return result;

	}

}