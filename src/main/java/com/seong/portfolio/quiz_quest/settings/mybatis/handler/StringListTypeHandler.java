package com.seong.portfolio.quiz_quest.settings.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        // 리스트를 문자열로 변환하여 저장
        if (parameter != null && !parameter.isEmpty()) {
            ps.setString(i, String.join(",", parameter)); // 리스트의 요소를 콤마로 구분하여 문자열로 변환
        } else {
            ps.setString(i, null); // 리스트가 비어있으면 NULL로 설정
        }
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 데이터베이스에서 문자열을 읽어 리스트로 변환
        String value = rs.getString(columnName);
        return value != null ? new ArrayList<>(Arrays.asList(value.split(","))) : null; // 문자열을 리스트로 변환
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value != null ? new ArrayList<>(Arrays.asList(value.split(","))) : null; // 문자열을 리스트로 변환
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value != null ? new ArrayList<>(Arrays.asList(value.split(","))) : null; // 문자열을 리스트로 변환
    }
}
