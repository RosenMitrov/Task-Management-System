package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.model.CommentDetailsDto;
import app.taskmanagementsystem.domain.dto.view.CommentDetailsViewDto;
import app.taskmanagementsystem.domain.dto.view.PostDetailsViewDto;
import app.taskmanagementsystem.security.AppUserDetails;
import app.taskmanagementsystem.services.CommentService;
import app.taskmanagementsystem.services.PostService;
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
@RequestMapping("/users/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @Autowired
    public CommentController(CommentService commentService,
                             PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @GetMapping("/to-post/{postId}")
    public String getCommentsByPostId(@PathVariable("postId") Long postId,
                                      Model model) {
        if (!model.containsAttribute("commentDetailsDto")) {
            model.addAttribute("commentDetailsDto", new CommentDetailsDto()
                    .setPostId(postId));
        }
        List<CommentDetailsViewDto> allCommentsDetailsViewByPostId = this.commentService.findAllCommentsDetailsViewByPostId(postId);
        PostDetailsViewDto postDetailsViewDtoById = this.postService.getPostDetailsViewDtoByPostId(postId);
        model.addAttribute("allCommentsByPostId", allCommentsDetailsViewByPostId);
        model.addAttribute("postDetailsViewDtoById", postDetailsViewDtoById);
        return "posts-comments";
    }

    @PostMapping("/to-post/{postId}")
    public String createCommentToPost(@Valid CommentDetailsDto commentDetailsDto,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      @PathVariable("postId") Long postId,
                                      @AuthenticationPrincipal
                                      AppUserDetails appUserDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentDetailsViewDto", commentDetailsDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.commentDetailsDto", bindingResult);
            return "redirect:/users/comments/to-post/" + postId;
        }

        this.commentService.createCommentToPostByPostId(postId, commentDetailsDto.setCreatorName(appUserDetails.getNickname()));
        return "redirect:/users/comments/to-post/" + postId;
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId,
                                @AuthenticationPrincipal
                                AppUserDetails appUserDetails) {
        boolean isOwner = this.commentService.checkIfDeleteOwnComment(commentId, appUserDetails.getNickname());
        Long postId;
        if (isOwner) {
            postId = this.commentService.deleteCommentById(commentId);
        } else {
            postId = this.commentService.getPostIdByCommentId(commentId);
        }
        return "redirect:/users/comments/to-post/" + postId;
    }
}
