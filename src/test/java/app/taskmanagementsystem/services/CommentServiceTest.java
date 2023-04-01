package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.model.CommentDetailsDto;
import app.taskmanagementsystem.domain.dto.view.CommentDetailsViewDto;
import app.taskmanagementsystem.domain.entity.*;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.repositories.CommentRepository;
import app.taskmanagementsystem.services.impl.CommentServiceImpl;
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
class CommentServiceTest {

    private CommentService commentServiceToTest;

    @Mock
    private CommentRepository mockCommentRepository;
    @Mock
    private PostService mockPostService;
    @Mock
    private UserService mockUserService;
    @Mock
    private ModelMapper mockModelMapper;

    @Captor
    private ArgumentCaptor<CommentEntity> commentEntityArgumentCaptor;


    @BeforeEach
    void setUp() {
        this.commentServiceToTest = new CommentServiceImpl(mockCommentRepository, mockPostService, mockUserService, mockModelMapper);
    }

    @Test
    void test_findAllCommentsDetailsViewByPostId_ShouldReturnThem_Successfully() {
        List<CommentEntity> expectedReturn = List.of(
                new CommentEntity()
                        .setMessage("First Comment!")
                        .setCreatorName("creatorName")
                        .setCreatedDate(LocalDateTime.now()),
                new CommentEntity()
                        .setMessage("Second Comment!")
                        .setCreatorName("creatorName2")
                        .setCreatedDate(LocalDateTime.now())
        );

        Mockito.when(this.mockCommentRepository.findAllByPost_Id(2L))
                .thenReturn(expectedReturn);

        Mockito.when(mockModelMapper.map(expectedReturn.get(0), CommentDetailsViewDto.class))
                .thenReturn(new CommentDetailsViewDto()
                        .setMessage(expectedReturn.get(0).getMessage())
                        .setCreatorName(expectedReturn.get(0).getCreatorName())
                        .setCreatedDate(expectedReturn.get(0).getCreatedDate()));

        Mockito.when(mockModelMapper.map(expectedReturn.get(1), CommentDetailsViewDto.class))
                .thenReturn(new CommentDetailsViewDto()
                        .setMessage(expectedReturn.get(1).getMessage())
                        .setCreatorName(expectedReturn.get(1).getCreatorName())
                        .setCreatedDate(expectedReturn.get(1).getCreatedDate()));

        List<CommentDetailsViewDto> actualCommentsReturned = this.commentServiceToTest.findAllCommentsDetailsViewByPostId(2L);

        Mockito.verify(mockModelMapper, Mockito.times(2))
                .map(Mockito.any(), ArgumentMatchers.eq(CommentDetailsViewDto.class));

        Assertions.assertEquals(expectedReturn.size(), actualCommentsReturned.size());

        Assertions.assertEquals(expectedReturn.get(0).getMessage(), actualCommentsReturned.get(0).getMessage());
        Assertions.assertEquals(expectedReturn.get(0).getCreatedDate(), actualCommentsReturned.get(0).getCreatedDate());
        Assertions.assertEquals(expectedReturn.get(0).getCreatorName(), actualCommentsReturned.get(0).getCreatorName());

        Assertions.assertEquals(expectedReturn.get(1).getMessage(), actualCommentsReturned.get(1).getMessage());
        Assertions.assertEquals(expectedReturn.get(1).getCreatedDate(), actualCommentsReturned.get(1).getCreatedDate());
        Assertions.assertEquals(expectedReturn.get(1).getCreatorName(), actualCommentsReturned.get(1).getCreatorName());

    }


    @Test
    void test_createCommentToPostByPostId_Successfully() {
        Long postId = 1L;
        CommentDetailsDto expectedData = new CommentDetailsDto()
                .setMessage("First Comment!")
                .setCreatorName("creatorName");

        Mockito.when(this.mockModelMapper.map(expectedData, CommentEntity.class))
                .thenReturn(new CommentEntity()
                        .setMessage(expectedData.getMessage())
                        .setCreatorName(expectedData.getCreatorName())
                        .setCreatedDate(expectedData.getCreatedDate()));

        this.commentServiceToTest.createCommentToPostByPostId(postId, expectedData);

        Mockito.verify(mockCommentRepository, Mockito.times(1))
                .saveAndFlush(this.commentEntityArgumentCaptor.capture());
        CommentEntity actualSavedComment = this.commentEntityArgumentCaptor.getValue();

        Assertions.assertEquals(expectedData.getMessage(), actualSavedComment.getMessage());
        Assertions.assertEquals(expectedData.getCreatorName(), actualSavedComment.getCreatorName());
    }

    @Test
    void test_deleteCommentById_ShouldDeleteItSuccessfully() {
        long commentId = 1L;
        long expectedPostId = 5L;
        PostEntity postEntity = (PostEntity) new PostEntity()
                .setTitle("Post title!")
                .setInformation("Post INFO")
                .setId(expectedPostId);

        CommentEntity expectedComment = new CommentEntity()
                .setMessage("First Comment!")
                .setCreatorName("creatorName")
                .setPost(postEntity);

        Mockito.when(this.mockCommentRepository.findById(commentId))
                .thenReturn(Optional.of(expectedComment));

        Long actualPostId = this.commentServiceToTest.deleteCommentById(commentId);

        Assertions.assertEquals(expectedPostId, actualPostId);
    }

    @Test
    void test_checkIfDeleteOwnComment_ShouldReturnTrue() {
        long commentId = 555L;
        String ownerCommentNickname = "ownerNickname";

        CommentEntity comment = new CommentEntity()
                .setMessage("First Comment!")
                .setCreatorName(ownerCommentNickname);

        Mockito.when(this.mockCommentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        boolean actualResult = this.commentServiceToTest.checkIfDeleteOwnComment(commentId, ownerCommentNickname);

        Assertions.assertTrue(actualResult);
    }


    @Test
    void test_checkIfDeleteOwnComment_ShouldReturnFalse() {
        long commentId = 555L;
        String ownerCommentNickname = "ownerNickname";
        String notOwnerNickname = "notOwnerNickname";

        CommentEntity comment = new CommentEntity()
                .setMessage("First Comment!")
                .setCreatorName(ownerCommentNickname);

        Mockito.when(this.mockCommentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        boolean actualResult = this.commentServiceToTest.checkIfDeleteOwnComment(commentId, notOwnerNickname);

        Assertions.assertFalse(actualResult);
    }

    @Test
    void test_getPostIdByCommentId_ShouldReturnItSuccessfully() {
        long commentId = 666L;
        String ownerCommentNickname = "ownerNickname";

        long expectedPostId = 22L;

        PostEntity post = (PostEntity) new PostEntity()
                .setTitle("Post title!")
                .setInformation("Post INFO")
                .setId(expectedPostId);

        CommentEntity comment = new CommentEntity()
                .setMessage("First Comment!")
                .setCreatorName(ownerCommentNickname)
                .setPost(post);

        Mockito.when(this.mockCommentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        Long actualPostId = this.commentServiceToTest.getPostIdByCommentId(commentId);

        Assertions.assertEquals(expectedPostId, actualPostId);
    }


    @Test
    void test_getCommentById_ShouldThrow() {
        Assertions.assertThrows(ObjNotFoundException.class, () -> {
            Long notFoundId = 55L;
            this.commentServiceToTest.getPostIdByCommentId(notFoundId);
        });
    }

    @Test
    void test_isDbInit_ShouldReturnTrue() {
        Mockito.when(mockCommentRepository.count())
                .thenReturn(50L);

        commentServiceToTest.commentsInitialization();

        Mockito
                .verify(mockCommentRepository, Mockito.times(0))
                .saveAllAndFlush(Mockito.any());
    }

    @Test
    void test_isDbInit_ShouldReturnFalse() {
        Mockito.when(mockCommentRepository.count())
                .thenReturn(0L);
        Mockito.when(this.mockUserService.getUserEntityByEmail("admin@adminov.bg"))
                .thenReturn(new UserEntity());
        Mockito.when(this.mockUserService.getUserEntityByEmail("moderator@moderatorov.bg"))
                .thenReturn(new UserEntity());
        Mockito.when(this.mockUserService.getUserEntityByEmail("user@userov.bg"))
                .thenReturn(new UserEntity());


        List<CommentEntity> expectedEntities = List.of(new CommentEntity()
                        .setMessage("First Comment")
                        .setCreatorName("testCreator"),
                new CommentEntity()
                        .setMessage("Second Comment")
                        .setCreatorName("testCreator2")
        );

        Mockito
                .when(mockCommentRepository.saveAllAndFlush(Mockito.any()))
                .thenReturn(expectedEntities);

        commentServiceToTest.commentsInitialization();
        Mockito
                .verify(mockCommentRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());

        List<CommentEntity> actualEntities = mockCommentRepository
                .saveAllAndFlush(Mockito.any());

        Assertions.assertEquals(expectedEntities, actualEntities);
        for (int index = 0; index < expectedEntities.size(); index++) {
            Assertions.assertEquals(expectedEntities.get(index), actualEntities.get(index));
            Assertions.assertEquals(expectedEntities.get(index).getCreatorName(), actualEntities.get(index).getCreatorName());
            Assertions.assertEquals(expectedEntities.get(index).getMessage(), actualEntities.get(index).getMessage());
        }
    }


}
