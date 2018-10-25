package com.cloud.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.amazonaws.auth.DefaultAWSCrentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
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
	@Value("${amazonProperties.bucket.name}")
	private String bucketName;
	

	@PostConstruct
	private void initializeAmazon() {
		this.s3client = AmazonS3ClientBuilder.standard()
						.withCredentials(new DefaultAWSCrentialsProviderChain())
						.build();
	}
	
	@Override
	public String uploadFile(MultipartFile multipartFile) throws Exception {
		
		String fileUrl = "";
	//	File file = convertMultiPartToFile(multipartFile);
		String fileName = Utils.generateFileName(multipartFile);
		fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
		uploadFileTos3bucket(fileName, multipartFile);
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
        FileOutputStream fos = new FileOutputStream("/opt/tomcat/uploads"+convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    private void uploadFileTos3bucket(String fileName, MultipartFile multipartFile) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
        s3client.putObject(new PutObjectRequest(bucketName, fileName, multipartFile.getInputStream(),metadata);
              //  .withCannedAcl(CannedAccessControlList.PublicRead));
    }
}
