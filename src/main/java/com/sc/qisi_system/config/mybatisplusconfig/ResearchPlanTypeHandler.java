package com.sc.qisi_system.config.mybatisplusconfig;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sc.qisi_system.module.apply.domain.ResearchPlanStage;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@MappedTypes(List.class)
public class ResearchPlanTypeHandler extends BaseTypeHandler<List<ResearchPlanStage>> {

    private static final Logger log = LoggerFactory.getLogger(ResearchPlanTypeHandler.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<ResearchPlanStage> parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            ps.setString(i, "[]");
        } else {
            String json = JSON.toJSONString(parameter);
            log.debug("保存 ResearchPlan: {}", json);
            ps.setString(i, json);
        }
    }

    @Override
    public List<ResearchPlanStage> getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String json = rs.getString(columnName);
        log.debug("读取 ResearchPlan (列名): {}", json);
        return parseJson(json);
    }

    @Override
    public List<ResearchPlanStage> getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String json = rs.getString(columnIndex);
        log.debug("读取 ResearchPlan (索引): {}", json);
        return parseJson(json);
    }

    @Override
    public List<ResearchPlanStage> getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String json = cs.getString(columnIndex);
        log.debug("读取 ResearchPlan (Callable): {}", json);
        return parseJson(json);
    }

    private List<ResearchPlanStage> parseJson(String json) {
        if (json == null || json.trim().isEmpty() || "null".equalsIgnoreCase(json.trim())) {
            log.warn("ResearchPlan JSON 为空");
            return new ArrayList<>();
        }

        try {
            JSONArray array = JSON.parseArray(json);
            if (array == null || array.isEmpty()) {
                log.warn("ResearchPlan JSON 数组为空");
                return new ArrayList<>();
            }

            List<ResearchPlanStage> result = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                log.debug("解析元素 {}: {}", i, obj);

                ResearchPlanStage stage = new ResearchPlanStage();

                // 安全获取字段值
                stage.setStage(obj.getString("stage"));
                stage.setContent(obj.getString("content"));

                Boolean completed = obj.getBoolean("completed");
                stage.setCompleted(completed != null ? completed : false);

                result.add(stage);
            }

            log.info("ResearchPlan 解析成功，共 {} 个阶段", result.size());
            return result;
        } catch (Exception e) {
            log.error("解析 ResearchPlan JSON 失败: {}", json, e);
            return new ArrayList<>();
        }
    }
}