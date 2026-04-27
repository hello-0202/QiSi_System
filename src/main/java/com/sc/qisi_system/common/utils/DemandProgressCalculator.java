package com.sc.qisi_system.common.utils;

import com.sc.qisi_system.module.apply.domain.ResearchPlanStage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class DemandProgressCalculator {

    public Integer calculateProgress(List<ResearchPlanStage> researchPlan) {

        if (researchPlan == null || researchPlan.isEmpty()) {
            return 0;
        }

        int total = researchPlan.size();
        long completed = researchPlan.stream()
                .filter(Objects::nonNull)
                .filter(stage -> Boolean.TRUE.equals(stage.getCompleted()))
                .count();

        return (int) ((completed * 100.0) / total);
    }
}
