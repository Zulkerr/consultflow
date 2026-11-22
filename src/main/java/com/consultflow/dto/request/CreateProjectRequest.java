package com.consultflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 100) String clientName,
        @NotBlank @Size(max = 50) String requiredSkill
) {}