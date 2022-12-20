package com.example.flabcaloriecounter.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.adapter.in.web.PhotoDto;

@Component
public class ImagePathHandler {

	// @Value("${file.dir}")
	private String fileDir;

	public PhotoDto storeImage(final MultipartFile multipartFile) {
		final StringBuilder newFileName = fileNameForSave(multipartFile);

		final StringBuilder filePathForSave = new StringBuilder()
			.append(this.fileDir)
			.append(File.separator)
			//todo userId추가..?
			//.append(userId)
			//.append(File.separator)
			.append(newFileName);

		try {
			multipartFile.transferTo(new File(this.fileDir + newFileName));
			return new PhotoDto(String.valueOf(newFileName), String.valueOf(filePathForSave));
		} catch (IOException e) {
			throw new FileUploadException(e);
		}
	}

	private StringBuilder fileNameForSave(final MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			return null;
		}

		String originalName = multipartFile.getOriginalFilename();
		String ext = originalName.substring(originalName.lastIndexOf(".") + 1);
		String uuid = UUID.randomUUID().toString();

		return new StringBuilder()
			.append(uuid)
			.append(".")
			.append(ext);
	}

	public List<PhotoDto> storeImages(final List<MultipartFile> multipartFiles) {
		return multipartFiles.stream()
			.filter(this::hasFile)
			.map(this::storeImage)
			.collect(Collectors.toList());
	}

	private boolean hasFile(MultipartFile multipartFile) {
		return !multipartFile.isEmpty();
	}
}
