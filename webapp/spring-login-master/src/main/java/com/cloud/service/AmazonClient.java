package com.cloud.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cloud.constants.CommonConstants;
import com.cloud.util.Utils;

@Service
@Profile("dev")
public class AmazonClient implements BaseClient{

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${spring.bucket.name}")
	private String bucketName;
	@Value("${accessKey}")
	private String accessKey;
	@Value("${secretKey}")
	private String secretKey;
 	
	@PostConstruct
	private void initializeAmazon() {
		BasicAWSCredentials creds = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).build();
	}

	
	
	public String test()
	{
		return "Access Key : " + accessKey;
	}
	
	@Override
	public String uploadFile(MultipartFile multipartFile) throws Exception {
		
		String fileUrl = "";
		File file = convertMultiPartToFile(multipartFile);
		String fileName = Utils.generateFileName(multipartFile);
		fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
		uploadFileTos3bucket(fileName, file);
		file.delete();

		return fileUrl;
	}
	
	@Override
	public String deleteFile(String fileUrl) throws Exception {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		return CommonConstants.DELETE_ATTACHMENTS_SUCCESS;
	}

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
}
