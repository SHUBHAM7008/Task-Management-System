package com.example.Task.Management.New.Repository;

import com.example.Task.Management.New.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
    // No need to define any additional methods unless necessary
}
