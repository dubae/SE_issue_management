package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.IssueCommentDTO;
import com.codingrecipe.member.entity.IssueCommentEntity;
import com.codingrecipe.member.entity.IssueEntity;
import com.codingrecipe.member.repository.IssueCommentRepository;
import com.codingrecipe.member.repository.IssueRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueCommentServiceTest {

    @Mock
    private IssueCommentRepository issueCommentRepository;

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueCommentService issueCommentService;

    private AutoCloseable closeable;

    private IssueEntity issueEntity1;
    private IssueCommentEntity comment1;
    private IssueCommentEntity comment2;
    private List<IssueCommentEntity> issueComments;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        issueEntity1  = IssueEntity.builder()
                .id(1L).title("title1").writerId("1L").description("test")
                .status("new").component("comp1")
                .build();


        comment1 = new IssueCommentEntity().builder()
                .id(1L).writerId("1L").content("content1").issueEntity(issueEntity1).build();

        comment2 = new IssueCommentEntity().builder()
                .id(2L).writerId("1L").content("content2").issueEntity(issueEntity1).build();

        issueComments = new ArrayList<>();
        issueComments.add(comment1);
        issueComments.add(comment2);
        issueEntity1.setComments(issueComments);





    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
        issueComments.clear();
    }

    @Test
    public void testFindByIssueId() {
        when(issueCommentRepository.findAll()).thenReturn(issueComments);

        List<IssueCommentDTO> result = issueCommentService.findByIssueId(1L);

        assertEquals(2, result.size());
        verify(issueCommentRepository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        IssueCommentDTO issueCommentDTO = new IssueCommentDTO();
        issueCommentDTO.setIssueId(1L);

        //when(issueRepository.findById(1L)).thenReturn(Optional.of(issueEntity));
        when(issueCommentRepository.save(any(IssueCommentEntity.class))).thenReturn(new IssueCommentEntity());

        issueCommentService.save(issueCommentDTO);

        verify(issueRepository, times(1)).findById(1L);
        verify(issueCommentRepository, times(1)).save(any(IssueCommentEntity.class));
    }

    @Test
    public void testFindById() {
        Long commentId = 1L;
        IssueCommentEntity issueCommentEntity = new IssueCommentEntity();
        issueCommentEntity.setId(commentId);

        when(issueCommentRepository.findById(commentId)).thenReturn(Optional.of(issueCommentEntity));

        IssueCommentDTO result = issueCommentService.findById(commentId);

        assertNotNull(result);
        assertEquals(commentId, result.getId());
        verify(issueCommentRepository, times(1)).findById(commentId);
    }

    @Test
    public void testFindAll() {
        when(issueCommentRepository.findAll()).thenReturn(issueComments);

        List<IssueCommentDTO> result = issueCommentService.findAll();

        assertEquals(2, result.size());
        verify(issueCommentRepository, times(1)).findAll();
    }
}
