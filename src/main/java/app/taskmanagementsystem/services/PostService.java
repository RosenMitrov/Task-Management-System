package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.model.PostAddDto;
import app.taskmanagementsystem.domain.dto.view.PostDetailsViewDto;
import app.taskmanagementsystem.domain.entity.PostEntity;

import java.util.List;

public interface PostService {

    void postsInitialization();

    PostEntity getPostEntityById(long postId);

    List<PostDetailsViewDto> findAllPostsByTaskId(Long taskId);

    void createNewPost(PostAddDto postAddDto, String username);
}
