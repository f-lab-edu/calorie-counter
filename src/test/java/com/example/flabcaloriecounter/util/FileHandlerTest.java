package com.example.flabcaloriecounter.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
class FileHandlerTest {

	@Autowired
	private FileHandler imagePathHandler;

	List<MultipartFile> files;

	MockMultipartFile image1;
	MockMultipartFile image2;

	@BeforeEach
	void setup() {
		files = new ArrayList<>();

		this.image1 = new MockMultipartFile(
			"feedDto",
			"photos",
			"image/jpeg",
			"photos".getBytes()
		);

		this.image2 = new MockMultipartFile(
			"feedDto",
			"photos2",
			"image/jpeg",
			"photos2".getBytes()
		);

		files.add(image1);
		files.add(image2);
	}

	@Test
	@DisplayName("파일이 정상적으로 있는경우 ")
	void storeImage() {
		assertThat(imagePathHandler.storeImages(files, 1).size()).isEqualTo(2);
	}
}