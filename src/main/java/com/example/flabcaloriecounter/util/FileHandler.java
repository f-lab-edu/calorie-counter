package com.example.flabcaloriecounter.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.exception.CustomException;
import com.example.flabcaloriecounter.exception.ExceptionFunction;
import com.example.flabcaloriecounter.feed.application.port.in.dto.PhotoDto;

@Component
public class FileHandler {

	private final String fileDir;

	public FileHandler(@Value("${file.dir}") final String fileDir) {
		this.fileDir = fileDir;
	}

	public List<PhotoDto> storeImages(final List<MultipartFile> multipartFiles, final long userId) {
		return multipartFiles.stream()
			.filter(multipartFile -> !multipartFile.isEmpty())
			.map(exceptionWrapper(multipartFile -> storeImage(multipartFile, userId)))
			.collect(Collectors.toList());
	}

	private <T, R> Function<T, R> exceptionWrapper(final ExceptionFunction<T, R> function) {
		return (T r) -> {
			try {
				return function.apply(r);
			} catch (AccessDeniedException e) {
				throw new CustomException(StatusEnum.FILE_PATH_NOT_FOUND, e);
			} catch (IOException e) {
				throw new CustomException(StatusEnum.FILE_SAVE_FAIL, e);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	private PhotoDto storeImage(final MultipartFile multipartFile, final long userId) throws IOException {
		final String fileName = newFileName(multipartFile, userId);
		final LocalDate now = LocalDate.now();

		final String filePath = new StringBuilder()
			.append(this.fileDir)
			.append(File.separator)
			.append(now.getYear())
			.append(File.separator)
			.append(now.getMonthValue())
			.append(now.getDayOfMonth())
			.append(File.separator).toString();

		makeDirectory(filePath);

		multipartFile.transferTo(new File(filePath + fileName));
		return new PhotoDto(fileName, filePath);
	}

	private String newFileName(final MultipartFile multipartFile, final long userId) {
		if (multipartFile.isEmpty()) {
			return "";
		}

		return newFileNameFormat(multipartFile, userId);
	}

	private void makeDirectory(final String filePathForSave) {
		File dirForSave = new File(filePathForSave);

		if (!dirForSave.exists()) {
			dirForSave.mkdir();
		}
	}

	private String newFileNameFormat(final MultipartFile multipartFile, final long userId) {
		final String originalName = multipartFile.getOriginalFilename();
		final String uuid = UUID.randomUUID().toString();
		final String ext = originalName.substring(originalName.lastIndexOf(".") + 1);

		return new StringBuilder()
			.append(uuid)
			.append("_")
			.append(userId)
			.append(".")
			.append(ext).toString();
	}
}
