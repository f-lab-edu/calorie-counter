package com.example.flabcaloriecounter.feed.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.adapter.in.web.PhotoDto;
import com.example.flabcaloriecounter.util.ImagePathHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImagePathHandler imagePathHandler;

	@Override
	public List<ImageUploadPath> uploadFile(final List<MultipartFile> photos) {
		List<PhotoDto> photoDtoResult = imagePathHandler.storeImages(photos);

		return photoDtoResult.stream()
			.map(photoDto -> new ImageUploadPath(photoDto.fileName(), photoDto.filePath()))
			.toList();
	}
}
