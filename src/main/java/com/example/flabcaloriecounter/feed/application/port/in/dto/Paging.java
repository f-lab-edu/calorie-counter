package com.example.flabcaloriecounter.feed.application.port.in.dto;

public record Paging(
	long cursorNo,
	int displayPerPage
) {
}
