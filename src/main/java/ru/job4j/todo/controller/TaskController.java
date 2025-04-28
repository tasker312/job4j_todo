package ru.job4j.todo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.category.CategoryService;
import ru.job4j.todo.service.priority.PriorityService;
import ru.job4j.todo.service.task.TaskService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/", "/tasks"})
public class TaskController {

    private final TaskService taskService;

    private final PriorityService priorityService;

    private final CategoryService categoryService;

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
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("task", task.orElse(new Task()));
        return "/tasks/_id";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task, @RequestParam List<Integer> categoriesId, Model model, HttpSession session) {
        task.setResponsible((User) session.getAttribute("user"));
        var categories = categoryService.findByIds(categoriesId);
        task.setCategories(new ArrayList<>(categories));
        var isSaved = taskService.create(task);
        if (!isSaved) {
            model.addAttribute("errorMessage", "Не удалось выполнить создание задачи Название '%s'.".formatted(task.getTitle()));
            return "/errors/error";
        }
        return "redirect:/tasks";
    }

    @PostMapping("/update")
    public String updateTask(@Valid @ModelAttribute Task task, Model model) {
        var isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("errorMessage", "Не удалось выполнить обновление задачи ID '%s'.".formatted(task.getId()));
            return "/errors/error";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete")
    public String deleteTask(@RequestParam int id, Model model) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("errorMessage", "Не удалось выполнить удаление задачи ID '%s'.".formatted(id));
            return "/errors/error";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/done")
    public String doneTask(@RequestParam int id, Model model) {
        var isUpdated = taskService.doneById(id);
        if (!isUpdated) {
            model.addAttribute("errorMessage", "Не удалось выполнить обновление задачи ID '%s'.".formatted(id));
            return "/errors/error";
        }
        return "redirect:/tasks";
    }

}
