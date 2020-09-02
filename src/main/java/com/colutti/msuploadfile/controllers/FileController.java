package com.colutti.msuploadfile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.colutti.msuploadfile.exceptions.FileException;
import com.colutti.msuploadfile.services.AmazonClient;

@RestController
@RequestMapping("/storage")
public class FileController {

	private AmazonClient amazonClient;

	@Autowired
	FileController(AmazonClient amazonClient) {
		this.amazonClient = amazonClient;
	}

	@PostMapping()
	public String uploadFile(@RequestPart(value = "file") MultipartFile file) throws FileException {
		return this.amazonClient.uploadFile(file);
	}

	@DeleteMapping()
	public String deleteFile(@RequestPart(value = "url") String fileUrl) throws FileException {
		return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
	}

}
