package com.example.Task.Management.New.controller;

import com.example.Task.Management.New.Exceptions.ResourceNotFoundException;
import com.example.Task.Management.New.Repository.ProjectRepository;
import com.example.Task.Management.New.model.Project;
import com.example.Task.Management.New.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        // Set the created date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createdDate = dateFormat.format(new Date());
        project.setCreatedDate(createdDate);

        // Set default status if not provided
        if (project.getStatus() == null || project.getStatus().isEmpty()) {
            project.setStatus("Not Started");
        }

        // Initialize tasks list if null
        if (project.getTasks() == null) {
            project.setTasks(List.of()); // Empty list
        }

        return ResponseEntity.status(201).body(projectRepository.save(project));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable String id, @RequestBody Project project) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        existingProject.setTitle(project.getTitle());
        existingProject.setDescription(project.getDescription());
        existingProject.setStatus(project.getStatus());
        existingProject.setAssociatedUsers(project.getAssociatedUsers());

        projectRepository.save(existingProject);
        return ResponseEntity.ok(existingProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable String id) {
        System.out.println("Detele !!!"+id);
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
        return ResponseEntity.ok("Project deleted successfully.");
    }

    @PutMapping("/{projectId}/tasks")
    public ResponseEntity<Project> addTaskToProject(@PathVariable String projectId, @RequestBody Task task) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (project.getTasks() == null) {
            project.setTasks(new ArrayList<>());
        }

        project.getTasks().add(String.valueOf(task)); // You can also set ID manually if needed
        projectRepository.save(project);
        return ResponseEntity.ok(project);
    }

}

