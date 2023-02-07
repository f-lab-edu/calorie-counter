package com.example.flabcaloriecounter.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.example.flabcaloriecounter.user.domain.UserType;

public class UserTypeHandler extends BaseTypeHandler<UserType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, UserType parameter, JdbcType jdbcType) throws
		SQLException {
		ps.setString(i, parameter.getCode());
	}

	@Override
	public UserType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return UserType.findByMessage(rs.getString(columnName));
	}

	@Override
	public UserType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return UserType.findByMessage(rs.getString(columnIndex));
	}

	@Override
	public UserType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return UserType.findByMessage(cs.getString(columnIndex));
	}

}

