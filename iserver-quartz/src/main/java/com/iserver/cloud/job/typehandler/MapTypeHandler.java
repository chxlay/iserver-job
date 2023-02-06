package com.iserver.cloud.job.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Map 类型数据库存储
 *
 * @author Alay
 * @date 2022-02-15 14:48
 */
@Component
@MappedJdbcTypes(value = JdbcType.VARCHAR)
@MappedTypes(value = {JSONObject.class})
public class MapTypeHandler implements TypeHandler<Map<String, String>> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int index, Map<String, String> map, JdbcType jdbcType) throws SQLException {
        if (null == map || map.isEmpty()) {
            preparedStatement.setString(index, null);
            return;
        }
        preparedStatement.setString(index, JSON.toJSONString(map));
    }

    @Override
    public Map<String, String> getResult(ResultSet resultSet, String columnName) throws SQLException {
        String jsonStr = resultSet.getString(columnName);
        return this.ofMap(jsonStr);
    }

    @Override
    public Map<String, String> getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String jsonStr = resultSet.getString(columnIndex);
        return this.ofMap(jsonStr);
    }

    @Override
    public Map<String, String> getResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String jsonStr = callableStatement.getString(columnIndex);
        return this.ofMap(jsonStr);
    }

    private Map<String, String> ofMap(String jsonStr) {
        if (null == jsonStr) {
            return null;
        }
        Map<String, String> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>() {
        });
        return map;
    }
}
