package exercise.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    private Task task;

    @BeforeEach
    public void setUp(){
        task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle),()->faker.lorem().word())
                .supply(Select.field(Task::getDescription),()->faker.lorem().paragraph(1))
                .create();
        taskRepository.save(task);
    }

    @AfterEach
    public void CleanUp(){
        taskRepository.deleteAll();
    }

    @Test
    public void testShow() throws Exception{
        var result = mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isEqualTo(om.writeValueAsString(task));
    }

    @Test
    public void testCreate() throws Exception{
        var newTask = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle),()->faker.lorem().word())
                .supply(Select.field(Task::getDescription),()->faker.lorem().paragraph())
                .create();
        var request = post("/tasks").
                contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newTask));
        var result = mockMvc.perform(request).andExpect(status().isCreated()).andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                a -> a.node("title").isEqualTo(newTask.getTitle()),
                a -> a.node("description").isEqualTo(newTask.getDescription())
        );
    }

    @Test
    public void testUpdate() throws Exception{
        var data  = new HashMap<>();
        data.put("title","newTite");
        data.put("description","new description");

        var request = put("/tasks/"+task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        var result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
          a -> a.node("title").isEqualTo(data.get("title")),
          a -> a.node("description").isEqualTo(data.get("description"))
        );
    }

    @Test
    public void testDelete() throws Exception{
        mockMvc.perform(delete("/tasks/"+task.getId())).andExpect(status().isOk());
    }
    // END
}
