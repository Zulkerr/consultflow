package com.consultflow.controller;

import com.consultflow.dto.request.CreateProjectRequest;
import com.consultflow.dto.response.ProjectResponse;
import com.consultflow.entity.Project;
import com.consultflow.service.ProjectService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@Valid @RequestBody CreateProjectRequest request) {
        return projectService.create(request);
    }

    @GetMapping
    public List<ProjectResponse> findAll(@RequestParam(required = false) Project.ProjectStatus status) {
        return projectService.findAll(status);
    }

    @GetMapping("/{id}")
    public ProjectResponse findById(@PathVariable Long id) {
        return projectService.findById(id);
    }

    @PatchMapping("/{id}/status")
    public ProjectResponse updateStatus(
            @PathVariable Long id,
            @RequestParam Project.ProjectStatus status) {
        return projectService.updateStatus(id, status);
    }
}