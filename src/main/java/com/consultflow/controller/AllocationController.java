package com.consultflow.controller;

import com.consultflow.dto.request.CreateAllocationRequest;
import com.consultflow.dto.response.AllocationResponse;
import com.consultflow.service.AllocationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/allocations")
@RequiredArgsConstructor
public class AllocationController {

    private final AllocationService allocationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AllocationResponse allocate(@Valid @RequestBody CreateAllocationRequest request) {
        return allocationService.allocate(request);
    }

    @GetMapping("/consultant/{consultantId}")
    public List<AllocationResponse> findByConsultant(@PathVariable Long consultantId) {
        return allocationService.findByConsultant(consultantId);
    }

    @GetMapping("/project/{projectId}")
    public List<AllocationResponse> findByProject(@PathVariable Long projectId) {
        return allocationService.findByProject(projectId);
    }
}