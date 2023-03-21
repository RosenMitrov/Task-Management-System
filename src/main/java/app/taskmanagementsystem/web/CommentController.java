package app.taskmanagementsystem.web;

import app.taskmanagementsystem.domain.dto.view.CommentDetailsViewDto;
import app.taskmanagementsystem.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/to-post/{postId}")
    public String getCommentsByPostId(@PathVariable("postId") Long postId,
                                      Model model) {
        List<CommentDetailsViewDto> allCommentsByPostId = this.commentService.findAllCommentsByPostId(postId);
        model.addAttribute("allCommentsByPostId", allCommentsByPostId);
        return "posts-comments";
    }
}
