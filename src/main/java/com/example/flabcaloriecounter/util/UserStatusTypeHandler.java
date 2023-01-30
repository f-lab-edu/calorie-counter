package com.example.flabcaloriecounter.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.example.flabcaloriecounter.user.domain.UserStatus;

public class UserStatusTypeHandler extends BaseTypeHandler<UserStatus> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, UserStatus parameter, JdbcType jdbcType)
		throws SQLException {
		ps.setString(i, parameter.getCode());
	}

	@Override
	public UserStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return UserStatus.findByMessage(rs.getString(columnName));
	}

	@Override
	public UserStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return UserStatus.findByMessage(rs.getString(columnIndex));
	}

	@Override
	public UserStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return UserStatus.findByMessage(cs.getString(columnIndex));
	}
}

