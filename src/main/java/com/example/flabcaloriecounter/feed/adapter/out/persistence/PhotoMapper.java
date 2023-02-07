package com.example.flabcaloriecounter.feed.adapter.out.persistence;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PhotoMapper {

	void delete(final long feedId);
}
