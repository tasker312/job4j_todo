package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.task.TaskService;

import java.util.Collection;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/", "/tasks"})
public class TaskController {

    private final TaskService taskService;

    @GetMapping("")
    public String getTaskList(@RequestParam(value = "type", defaultValue = "all") String type, Model model) {
        Collection<Task> tasks = taskService.findAll();
        model.addAttribute("type", type);
        model.addAttribute("tasks", tasks);
        return "/tasks/list";
    }

    @GetMapping("/{id}")
    public String getTask(@PathVariable int id, @RequestParam(value = "type", defaultValue = "read") String type, Model model) {
        Optional<Task> task = taskService.findById(id);
        model.addAttribute("type", type);
        if (task.isEmpty()) {
            model.addAttribute("type", "edit");
        }
        model.addAttribute("task", task.orElse(new Task()));
        return "/tasks/_id";
    }

    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete")
    public String deleteTask(@RequestParam int id) {
        taskService.deleteById(id);
        return "redirect:/tasks";
    }

    @GetMapping("/done")
    public String doneTask(@RequestParam int id) {
        taskService.doneById(id);
        return "redirect:/tasks";
    }

}
