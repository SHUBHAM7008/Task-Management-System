package com.example.Task.Management.New.Repository;

import com.example.Task.Management.New.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByAssignedUserId(String userId);

}
