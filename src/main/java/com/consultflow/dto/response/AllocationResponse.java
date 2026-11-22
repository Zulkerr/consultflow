package com.consultflow.dto.response;

import com.consultflow.entity.Allocation;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AllocationResponse(
        Long id,
        Long consultantId,
        String consultantName,
        Long projectId,
        String projectTitle,
        int allocationPercent,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime createdAt
) {
    public static AllocationResponse from(Allocation a) {
        return new AllocationResponse(
                a.getId(),
                a.getConsultant().getId(),
                a.getConsultant().getName(),
                a.getProject().getId(),
                a.getProject().getTitle(),
                a.getAllocationPercent(),
                a.getStartDate(),
                a.getEndDate(),
                a.getCreatedAt()
        );
    }
}