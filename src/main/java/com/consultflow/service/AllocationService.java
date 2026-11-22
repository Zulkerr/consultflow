package com.consultflow.service;

import com.consultflow.dto.request.CreateAllocationRequest;
import com.consultflow.dto.response.AllocationResponse;
import com.consultflow.entity.Allocation;
import com.consultflow.entity.Consultant;
import com.consultflow.entity.Project;
import com.consultflow.exception.BusinessRuleException;
import com.consultflow.exception.ResourceNotFoundException;
import com.consultflow.repository.AllocationRepository;
import com.consultflow.repository.ConsultantRepository;
import com.consultflow.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AllocationService {

    private final AllocationRepository allocationRepository;
    private final ConsultantRepository consultantRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public AllocationResponse allocate(CreateAllocationRequest request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new BusinessRuleException("End date must not be before start date");
        }

        Consultant consultant = consultantRepository.findById(request.consultantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultant not found with id: " + request.consultantId()));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found with id: " + request.projectId()));

        if (project.getStatus() == Project.ProjectStatus.COMPLETED) {
            throw new BusinessRuleException("Cannot allocate consultant to a COMPLETED project");
        }

        if (!consultant.getPrimarySkill().equalsIgnoreCase(project.getRequiredSkill())) {
            throw new BusinessRuleException(
                    "Skill mismatch: consultant has '" + consultant.getPrimarySkill()
                    + "' but project requires '" + project.getRequiredSkill() + "'");
        }

        int currentLoad = allocationRepository.sumOverlappingAllocationPercent(
                consultant.getId(), request.startDate(), request.endDate());
        int totalLoad = currentLoad + request.allocationPercent();

        if (totalLoad > consultant.getMaxCapacityPercent()) {
            throw new BusinessRuleException(
                    "Capacity exceeded: consultant '" + consultant.getName()
                    + "' already has " + currentLoad + "% allocated (max "
                    + consultant.getMaxCapacityPercent() + "%)");
        }

        Allocation allocation = Allocation.builder()
                .consultant(consultant)
                .project(project)
                .allocationPercent(request.allocationPercent())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        return AllocationResponse.from(allocationRepository.save(allocation));
    }

    public List<AllocationResponse> findByConsultant(Long consultantId) {
        if (!consultantRepository.existsById(consultantId)) {
            throw new ResourceNotFoundException("Consultant not found with id: " + consultantId);
        }
        return allocationRepository.findByConsultantId(consultantId).stream()
                .map(AllocationResponse::from)
                .toList();
    }

    public List<AllocationResponse> findByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        return allocationRepository.findByProjectId(projectId).stream()
                .map(AllocationResponse::from)
                .toList();
    }
}