package com.consultflow.service;

import com.consultflow.dto.request.CreateProjectRequest;
import com.consultflow.dto.response.ProjectResponse;
import com.consultflow.entity.Project;
import com.consultflow.exception.BusinessRuleException;
import com.consultflow.exception.ResourceNotFoundException;
import com.consultflow.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponse create(CreateProjectRequest request) {
        Project project = Project.builder()
                .title(request.title())
                .clientName(request.clientName())
                .requiredSkill(request.requiredSkill())
                .status(Project.ProjectStatus.DRAFT)
                .build();
        return ProjectResponse.from(projectRepository.save(project));
    }

    public List<ProjectResponse> findAll(Project.ProjectStatus status) {
        List<Project> projects = (status != null)
                ? projectRepository.findByStatus(status)
                : projectRepository.findAll();
        return projects.stream().map(ProjectResponse::from).toList();
    }

    public ProjectResponse findById(Long id) {
        return projectRepository.findById(id)
                .map(ProjectResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    @Transactional
    public ProjectResponse updateStatus(Long id, Project.ProjectStatus newStatus) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        if (project.getStatus() == Project.ProjectStatus.COMPLETED) {
            throw new BusinessRuleException("Completed projects cannot change status");
        }
        project.setStatus(newStatus);
        return ProjectResponse.from(projectRepository.save(project));
    }
}