package com.example.flabcaloriecounter.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.example.flabcaloriecounter.feed.domain.LikeStatus;

public class LikeStatusHandler extends BaseTypeHandler<LikeStatus> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, LikeStatus parameter, JdbcType jdbcType) throws
		SQLException {
		ps.setString(i, parameter.getStatusMessage());
	}

	@Override
	public LikeStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return LikeStatus.findByMessage(rs.getString(columnName));
	}

	@Override
	public LikeStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return LikeStatus.findByMessage(rs.getString(columnIndex));
	}

	@Override
	public LikeStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return LikeStatus.findByMessage(cs.getString(columnIndex));
	}
}

