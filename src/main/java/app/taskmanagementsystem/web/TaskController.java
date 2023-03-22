package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.model.TaskAddDto;
import app.taskmanagementsystem.domain.dto.view.TaskDetailsViewDto;
import app.taskmanagementsystem.security.AppUserDetails;
import app.taskmanagementsystem.services.TaskService;
import app.taskmanagementsystem.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService,
                          UserService userService) {
        this.taskService = taskService;

        this.userService = userService;
    }

    @GetMapping("/all")
    public String getTasks(@AuthenticationPrincipal AppUserDetails appUserDetails,
                           Model model) {
        List<TaskDetailsViewDto> allTasksDetailsViews = this.taskService.getAllTasksDetailsViews(appUserDetails.getUsername());
        model.addAttribute("allTasksDetailsViews", allTasksDetailsViews);
        return "tasks-all";
    }

    @PatchMapping("/map-task/{taskId}")
    public String getTaskById(@PathVariable("taskId") Long taskId,
                              @AuthenticationPrincipal AppUserDetails appUserDetails) {
        this.taskService.assignUserToTask(taskId, appUserDetails.getUsername());
        return "redirect:/users/tasks/all";
    }

    @PatchMapping("/detach-user-from-task/{taskId}")
    public String declineTaskById(@PathVariable("taskId") Long taskId,
                              @AuthenticationPrincipal AppUserDetails appUserDetails) {
        this.taskService.removeUserFromTaskById(taskId, appUserDetails.getUsername());
        return "redirect:/users/tasks/all";
    }

    @GetMapping("/add-task")
    public String addTask(Model model) {

        if (!model.containsAttribute("taskAddDto")) {
            model.addAttribute("taskAddDto", new TaskAddDto());
        }

        return "tasks-add";
    }

    @PostMapping("/add-task")
    public String createTask(@Valid TaskAddDto taskAddDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal
                             AppUserDetails appUserDetails) {
        if (appUserDetails == null) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("taskAddDto", taskAddDto);
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.taskAddDto", bindingResult);
            return "redirect:/users/tasks/add-task";
        }

        this.taskService.createTask(taskAddDto, appUserDetails.getNickname());

        return "redirect:/users/tasks/all";
    }


}
