package com.example.flabcaloriecounter.util;

import static com.example.flabcaloriecounter.exception.GlobalExceptionHandler.FILE_STORE_FAIL_MSG;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.flabcaloriecounter.exception.ExceptionFunction;
import com.example.flabcaloriecounter.exception.FileUploadException;
import com.example.flabcaloriecounter.feed.application.port.in.dto.PhotoDto;

@Component
@Profile("test")
public class LocalFileHandler implements FileHandler {

	private final String fileDir;

	public LocalFileHandler(@Value("${file.test.dir}") final String fileDir) {
		this.fileDir = fileDir;
	}

	@Override
	public List<PhotoDto> storeImages(final List<MultipartFile> multipartFiles, final long userId) {
		return multipartFiles.stream()
			.filter(this::hasFile)
			.map(exceptionWrapper(multipartFile -> storeImage(multipartFile, userId)))
			.collect(Collectors.toList());
	}

	private boolean hasFile(final MultipartFile multipartFile) {
		return !multipartFile.isEmpty();
	}

	private <T, R> Function<T, R> exceptionWrapper(final ExceptionFunction<T, R> function) {
		return (T r) -> {
			try {
				return function.apply(r);
			} catch (AccessDeniedException e) {
				throw new FileUploadException("해당 경로의 파일을 식별할수 없습니다.", e);
			} catch (IOException e) {
				throw new FileUploadException(FILE_STORE_FAIL_MSG, e);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	@Override
	public PhotoDto storeImage(final MultipartFile multipartFile, final long userId) throws IOException {
		final StringBuilder fileName = newFileName(multipartFile, userId);
		final LocalDate now = LocalDate.now();

		final StringBuilder filePath = new StringBuilder()
			.append(this.fileDir)
			.append(File.separator)
			.append(now.getYear())
			.append(File.separator)
			.append(now.getMonthValue())
			.append(now.getDayOfMonth())
			.append(File.separator);

		makeDirectory(filePath);

		multipartFile.transferTo(new File(String.valueOf(filePath.append(fileName))));
		return new PhotoDto(String.valueOf(fileName), String.valueOf(filePath));
	}

	private StringBuilder newFileName(final MultipartFile multipartFile, final long userId) {
		if (multipartFile.isEmpty()) {
			return new StringBuilder();
		}

		return newFileNameFormat(multipartFile, userId);
	}

	private void makeDirectory(final StringBuilder filePathForSave) {
		File dirForSave = new File(String.valueOf(filePathForSave));

		if (!dirForSave.exists()) {
			dirForSave.mkdir();
		}
	}

	private StringBuilder newFileNameFormat(final MultipartFile multipartFile, final long userId) {
		final String originalName = multipartFile.getOriginalFilename();
		final String uuid = UUID.randomUUID().toString();
		final String ext = originalName.substring(originalName.lastIndexOf(".") + 1);

		return new StringBuilder()
			.append(uuid)
			.append("_")
			.append(userId)
			.append(".")
			.append(ext);
	}
}
