package com.codingrecipe.member.repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.codingrecipe.member.entity.ProjectEntity;

public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void findAll() {

    }

    @Test
    public void testRegister() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setProjectname("name");
        projectEntity.setProjectdescription("description");
        projectEntity.setProjectstatus("status");
        projectEntity.setProjectcreatedtime("createdtime");
        projectRepository.save(projectEntity);
    }

}
