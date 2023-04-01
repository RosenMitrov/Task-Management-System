package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.model.PostAddDto;
import app.taskmanagementsystem.domain.dto.view.PostDetailsViewDto;
import app.taskmanagementsystem.domain.entity.PostEntity;
import app.taskmanagementsystem.domain.entity.TaskEntity;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.helper.TestDataHelper;
import app.taskmanagementsystem.repositories.PostRepository;
import app.taskmanagementsystem.services.impl.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private PostService postServiceToTest;

    private TestDataHelper testDataHelper;
    @Mock
    private PostRepository mockPostRepository;
    @Mock
    private UserService mockUserService;
    @Mock
    private TaskService mockTaskService;
    @Mock
    private ModelMapper mockModelMapper;

    @Captor
    private ArgumentCaptor<PostEntity> postEntityArgumentCaptor;

    @BeforeEach
    void setUp() {
        this.postServiceToTest = new PostServiceImpl(this.mockPostRepository, this.mockUserService, this.mockTaskService, this.mockModelMapper);
        this.testDataHelper = new TestDataHelper();
    }

    @Test
    void test_getPostEntityById_ShouldThrow() {
        Assertions.assertThrows(ObjNotFoundException.class, () -> {
            this.postServiceToTest.getPostEntityById(1L);
        });
    }

    @Test
    void test_getPostEntityById_ShouldReturnItSuccessfully() {
        PostEntity testPost = this.testDataHelper.createPostONE();

        Mockito.when(mockPostRepository.findById(999L))
                .thenReturn(Optional.of(testPost));

        PostEntity postReturned = this.postServiceToTest.getPostEntityById(999L);

        Assertions.assertEquals(testPost.getTitle(), postReturned.getTitle());
        Assertions.assertEquals(testPost.getInformation(), postReturned.getInformation());
        Assertions.assertEquals(testPost.getCreatorName(), postReturned.getCreatorName());
        Assertions.assertEquals(testPost.getCreatedDate(), postReturned.getCreatedDate());
        Assertions.assertEquals(testPost.getComments(), postReturned.getComments());
    }

    @Test
    void test_findAllPostDetailsViewsByTaskId_ShouldReturnThemSuccessfully() {
        PostEntity firstPost = new PostEntity()
                .setTitle("Test");
        PostEntity secondPost = new PostEntity()
                .setInformation("Information!");

        Mockito.when(mockPostRepository.findAllByTask_Id(99L))
                .thenReturn(List.of(firstPost, secondPost));

        Mockito.when(mockModelMapper.map(firstPost, PostDetailsViewDto.class))
                .thenReturn(new PostDetailsViewDto().setTitle("Test"));

        Mockito.when(mockModelMapper.map(secondPost, PostDetailsViewDto.class))
                .thenReturn(new PostDetailsViewDto().setInformation("Information!"));

        List<PostDetailsViewDto> returnedPosts = this.postServiceToTest.findAllPostDetailsViewsByTaskId(99L);

        Assertions.assertEquals(2, returnedPosts.size());
        Assertions.assertEquals(firstPost.getTitle(), returnedPosts.get(0).getTitle());
        Assertions.assertEquals(secondPost.getInformation(), returnedPosts.get(1).getInformation());
        Mockito.verify(mockModelMapper, Mockito.times(2)).map(Mockito.any(), ArgumentMatchers.eq(PostDetailsViewDto.class));
    }

    @Test
    void test_createNewPost_ShouldCreateIt() {
        PostAddDto postAddDto = new PostAddDto()
                .setTitle("Post 1 DTO")
                .setInformation("INFO to Post 1 DTO")
                .setTaskId(22L);
        String username = "testName";

        TaskEntity taskByIdReturned = (TaskEntity) new TaskEntity()
                .setTitle("Task from DB").setId(22L);

        Mockito.when(this.mockTaskService.getTaskEntityByTaskId(postAddDto.getTaskId()))
                .thenReturn(taskByIdReturned);

        this.postServiceToTest.createNewPost(postAddDto, username);

        Mockito.verify(this.mockPostRepository, Mockito.times(1))
                .saveAndFlush(this.postEntityArgumentCaptor.capture());

        PostEntity savedPost = this.postEntityArgumentCaptor.getValue();

        Mockito.verify(mockModelMapper, Mockito.times(1))
                .map(Mockito.any(), ArgumentMatchers.eq(PostDetailsViewDto.class));

        Assertions.assertEquals(username, savedPost.getCreatorName());
        Assertions.assertEquals(postAddDto.getTitle(), savedPost.getTitle());
        Assertions.assertEquals(postAddDto.getInformation(), savedPost.getInformation());
        Assertions.assertEquals(postAddDto.getTaskId(), savedPost.getTask().getId());
    }


    @Test
    void test_createNewPost_Invoke_saveAndFlushSuccessfully() {
        PostAddDto postAddDto = new PostAddDto()
                .setTitle("Post 1 DTO")
                .setInformation("INFO to Post 1 DTO")
                .setTaskId(22L);
        String username = "testName";

        this.postServiceToTest.createNewPost(postAddDto, username);

        Mockito.verify(this.mockPostRepository).saveAndFlush(Mockito.any());
    }


    @Test
    void test_getPostDetailsViewDtoByPostId_ShouldThrow(){
        Assertions.assertThrows(ObjNotFoundException.class, () -> {
            this.postServiceToTest.getPostDetailsViewDtoByPostId(1L);
        });
    }


    @Test
    void test_getPostDetailsViewDtoByPostId_ShouldReturnItSuccessfully() {
        PostEntity postEntityTest = new PostEntity()
                .setTitle("Post title 1")
                .setCreatorName("testName")
                .setCreatedDate(LocalDateTime.now())
                .setInformation("Test Info");

        Mockito.when(this.mockPostRepository.findById(1L))
                .thenReturn(Optional.of(postEntityTest));

        Mockito.when(mockModelMapper.map(postEntityTest, PostDetailsViewDto.class))
                .thenReturn(new PostDetailsViewDto()
                        .setTitle(postEntityTest.getTitle())
                        .setCreatorName(postEntityTest.getCreatorName())
                        .setCreatedDate(postEntityTest.getCreatedDate())
                        .setInformation(postEntityTest.getInformation()));

        PostDetailsViewDto returnedView = this.postServiceToTest.getPostDetailsViewDtoByPostId(1L);

        Mockito.verify(mockModelMapper, Mockito.times(1))
                .map(Mockito.any(), ArgumentMatchers.eq(PostDetailsViewDto.class));

        Assertions.assertEquals(postEntityTest.getTitle(), returnedView.getTitle());
        Assertions.assertEquals(postEntityTest.getCreatedDate(), returnedView.getCreatedDate());
        Assertions.assertEquals(postEntityTest.getCreatorName(), returnedView.getCreatorName());
        Assertions.assertEquals(postEntityTest.getInformation(), returnedView.getInformation());

    }
}
