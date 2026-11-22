package com.consultflow.dto.response;

import com.consultflow.entity.Project;
import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String title,
        String clientName,
        String requiredSkill,
        Project.ProjectStatus status,
        LocalDateTime createdAt
) {
    public static ProjectResponse from(Project p) {
        return new ProjectResponse(
                p.getId(),
                p.getTitle(),
                p.getClientName(),
                p.getRequiredSkill(),
                p.getStatus(),
                p.getCreatedAt()
        );
    }
}