package com.colutti.msuploadfile.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.colutti.msuploadfile.exceptions.FileException;

@Service
public class AmazonClient {

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.accessKey}")
	private String accessKey;

	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(new PutObjectRequest(this.bucketName, fileName, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String uploadFile(MultipartFile multipartFile) throws FileException {

		try {
			String fileUrl = "";
			try {
				File file = convertMultiPartToFile(multipartFile);
				String fileName = generateFileName(multipartFile);
				fileUrl = this.endpointUrl + "/" + this.bucketName + "/" + fileName;
				uploadFileTos3bucket(fileName, file);
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return fileUrl;
		} catch (Exception e) {
			throw new FileException("Error an error when uploading the file");
		}

	}

	public String deleteFileFromS3Bucket(String fileUrl) throws FileException {
		try {
			String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			s3client.deleteObject(new DeleteObjectRequest(this.bucketName + "/", fileName));
			return "Successfully deleted";
		} catch (Exception e) {
			throw new FileException("Error an error when deleting the file");
		}

	}

}
