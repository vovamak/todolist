package com.example.todolist.controller;


import com.example.todolist.domain.Status;
import com.example.todolist.domain.Task;
import com.example.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final int DEFAULT_PAGE_SIZE = 5;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String listTasks(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Task> taskPage = taskService.findAll(page - 1, size);
        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        int totalPages = taskPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "tasks";
    }
    @GetMapping("/new")
        public String showTaskForm (Model model){
            model.addAttribute("task", new Task());
            model.addAttribute("statuses", Status.values());
            return "task-form";
    }
    @PostMapping("/save")
    public String saveTask(
            @Valid @ModelAttribute("task") Task task,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "task-form";
        }

        taskService.save(task);
        redirectAttributes.addFlashAttribute("message", "Task saved successfully");
        return "redirect:/tasks";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        model.addAttribute("statuses", Status.values());
        return "task-form";
    }
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Task deleted successfully");
        return "redirect:/tasks";
    }




}
