package com.seong.portfolio.quiz_quest.settings.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class SetObjectTypeHandler extends BaseTypeHandler<LinkedHashSet<Object>> {



    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LinkedHashSet<Object> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null && !parameter.isEmpty()) {
            String joined = String.join(",", parameter.stream().map(Object::toString).toArray(String[]::new));
            ps.setString(i, joined);
        } else {
            ps.setString(i, null);
        }
    }

    @Override
    public LinkedHashSet<Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value != null ?
                Arrays.stream(value.split(","))
                        .map(obj -> (Object) obj) // Object로 변환
                        .collect(Collectors.toCollection(LinkedHashSet::new)) : null;
    }

    @Override
    public LinkedHashSet<Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value != null ?
                Arrays.stream(value.split(","))
                        .map(obj -> (Object) obj) // Object로 변환
                        .collect(Collectors.toCollection(LinkedHashSet::new)) : null;
    }

    @Override
    public LinkedHashSet<Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value != null ?
                Arrays.stream(value.split(","))
                        .map(obj -> (Object) obj) // Object로 변환
                        .collect(Collectors.toCollection(LinkedHashSet::new)) : null;
    }
}
