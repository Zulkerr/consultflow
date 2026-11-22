package com.consultflow.repository;

import com.consultflow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(Project.ProjectStatus status);
}