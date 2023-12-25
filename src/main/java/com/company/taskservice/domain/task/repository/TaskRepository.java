package com.company.taskservice.domain.task.repository;

import com.company.taskservice.domain.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
