package com.sc.qisi_system.module.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sc.qisi_system.config.mybatisplusconfig.ResearchPlanTypeHandler;
import com.sc.qisi_system.module.practice.entity.DemandExecutionPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DemandExecutionPlanMapper extends BaseMapper<DemandExecutionPlan> {

    @Select("SELECT * FROM demand_execution_plan WHERE demand_id = #{demandId}")
    @Results(id = "planResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "demandId", column = "demand_id"),
            @Result(property = "applyId", column = "apply_id"),
            @Result(property = "researchPlan", column = "research_plan",
                    typeHandler = ResearchPlanTypeHandler.class),
            @Result(property = "expectedFinishTime", column = "expected_finish_time"),
            @Result(property = "version", column = "version"),
            @Result(property = "operatorId", column = "operator_id"),
            @Result(property = "modifyRemark", column = "modify_remark"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    DemandExecutionPlan selectByDemandIdWithHandler(Long demandId);
}
