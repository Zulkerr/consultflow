package com.consultflow.dto.response;

import com.consultflow.entity.Consultant;
import java.time.LocalDateTime;

public record ConsultantResponse(
        Long id,
        String name,
        String email,
        String primarySkill,
        int maxCapacityPercent,
        LocalDateTime createdAt
) {
    public static ConsultantResponse from(Consultant c) {
        return new ConsultantResponse(
                c.getId(),
                c.getName(),
                c.getEmail(),
                c.getPrimarySkill(),
                c.getMaxCapacityPercent(),
                c.getCreatedAt()
        );
    }
}