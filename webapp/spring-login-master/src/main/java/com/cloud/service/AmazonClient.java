package com.cloud.service;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cloud.constants.CommonConstants;
import com.cloud.util.Utils;

@Service
@Profile("dev")
public class AmazonClient implements BaseClient{

	private AmazonS3 s3client;
	
	private static final String DIRECTORY = "Images/";

    private static final String UNDERSCORE = "_";

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${spring.bucket.name}")
	private String bucketName;
	//@Value("${amazonProperties.bucketName}")
	//private String bucketName;

	@PostConstruct
	private void initializeAmazon() {
		this.s3client = AmazonS3ClientBuilder.standard()
						.withCredentials(new DefaultAWSCredentialsProviderChain())
						.build();
	}
	
	@Override
	public String uploadFile(MultipartFile multipartFile, String userId) throws Exception {
		
		String name = Utils.generateFileName(multipartFile);
        String fileName =  DIRECTORY + userId + UNDERSCORE + name;

        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        s3client.putObject(bucketName, fileName, inputStream, new ObjectMetadata());
        
        return fileName;
		
	}
	
	@Override
	public String deleteFile(String fileUrl) throws Exception {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		return CommonConstants.DELETE_ATTACHMENTS_SUCCESS;
	}
}
