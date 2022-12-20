package com.example.flabcaloriecounter.feed.application.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * 이미지를 저장하는 장소별로 다르게 구현될것같아 인터페이스로 생성
 */
public interface ImageService {

	List<ImageUploadPath> uploadFile(final List<MultipartFile> photos);
}
