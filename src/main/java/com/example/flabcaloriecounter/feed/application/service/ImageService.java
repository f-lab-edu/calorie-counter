package com.example.flabcaloriecounter.feed.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.application.port.in.dto.ImageUploadPath;
import com.example.flabcaloriecounter.util.FileHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final FileHandler fileHandler;

	public List<ImageUploadPath> uploadFile(final List<MultipartFile> photos, final long userId) {
		return fileHandler.storeImages(photos, userId).stream()
			.map(photoDto -> new ImageUploadPath(photoDto.fileName(), photoDto.filePath()))
			.toList();
	}
}
