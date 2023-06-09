package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.model.PostAddDto;
import app.taskmanagementsystem.domain.dto.view.PostDetailsViewDto;
import app.taskmanagementsystem.domain.dto.view.TaskDetailsViewDto;
import app.taskmanagementsystem.security.AppUserDetails;
import app.taskmanagementsystem.services.PostService;
import app.taskmanagementsystem.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users/posts")
public class PostController {

    private final PostService postService;
    private final TaskService taskService;

    @Autowired
    public PostController(PostService postService,
                          TaskService taskService) {
        this.postService = postService;
        this.taskService = taskService;
    }


    @GetMapping("/related-to-task-id/{taskId}")
    public String getTasksPosts(@PathVariable("taskId") Long taskId,
                                Model model) {
        List<PostDetailsViewDto> allPostsByTaskId = this.postService.findAllPostDetailsViewsByTaskId(taskId);
        model.addAttribute("allPostsByTaskId", allPostsByTaskId);
        TaskDetailsViewDto taskDetailsViewByTaskId = this.taskService.getTaskDetailsViewByTaskId(taskId);
        model.addAttribute("taskView", taskDetailsViewByTaskId);
        return "posts-all";
    }

    @GetMapping("/create-new-post-to-task/{taskId}")
    public String getCreationPage(@PathVariable("taskId") Long taskId,
                                  Model model) {
        if (!model.containsAttribute("postAddDto")) {
            model.addAttribute("postAddDto", new PostAddDto().setTaskId(taskId));
        }
        return "posts-add";
    }

    @PostMapping("/create-new-post")
    public String createNewPost(@Valid PostAddDto postAddDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal AppUserDetails appUserDetails) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("postAddDto", postAddDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.postAddDto", bindingResult);
            return "redirect:/users/posts/create-new-post-to-task/" + postAddDto.getTaskId();
        }
        if (appUserDetails != null) {
            PostDetailsViewDto createdPost = this.postService.createNewPost(postAddDto, appUserDetails.getNickname());
            return "redirect:/users/posts/related-to-task-id/" + createdPost.getTask().getId();
        }
        return "redirect:/users/tasks/all";
    }


}
