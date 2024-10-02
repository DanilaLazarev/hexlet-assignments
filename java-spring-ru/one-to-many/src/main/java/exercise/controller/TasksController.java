package exercise.controller;

import java.util.List;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.mapper.UserMapper;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    // BEGIN
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public List<TaskDTO> index(){
        return taskRepository.findAll().stream().map(p -> taskMapper.map(p)).toList();
    }

    @GetMapping("/{id}")
    public TaskDTO show(@PathVariable Long id){
        var task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not exist"));
        return taskMapper.map(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO createDTO){
        var task = taskMapper.map(createDTO);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@PathVariable Long id,@Valid @RequestBody TaskUpdateDTO updateDTO){
        var task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not exist"));
        taskMapper.update(updateDTO,task);
        if(!(updateDTO.getAssigneeId()==null)){
            var user = userRepository.findById(updateDTO.getAssigneeId()).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not exist"));
            task.setAssignee(user);
        }
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        taskRepository.deleteById(id);
    }
    // END
}
