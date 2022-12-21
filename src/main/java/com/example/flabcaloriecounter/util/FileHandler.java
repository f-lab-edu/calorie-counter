package com.example.flabcaloriecounter.util;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.feed.application.port.in.dto.PhotoDto;

public interface FileHandler {

	List<PhotoDto> storeImages(final List<MultipartFile> multipartFiles, final long userId);

	PhotoDto storeImage(final MultipartFile multipartFile, final long userId) throws IOException;
}
