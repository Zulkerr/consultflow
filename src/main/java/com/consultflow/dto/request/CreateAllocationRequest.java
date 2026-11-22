package com.consultflow.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateAllocationRequest(
        @NotNull Long consultantId,
        @NotNull Long projectId,
        @NotNull @Min(1) @Max(100) Integer allocationPercent,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}