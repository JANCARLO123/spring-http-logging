package pl.piomin.spring.logging.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;
import pl.piomin.spring.logging.domain.TestExample;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ExampleControllerTests {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testPostMdc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/example").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new TestExample(1, "Test"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assert.notNull(MDC.get("time"), "Time empty");
        Assert.notNull(MDC.get("method"), "Method empty");
        Assert.isTrue(MDC.get("method").equalsIgnoreCase("POST"), "Method invalid");
    }

    @Test
    public void testPutMdc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/example").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new TestExample(1, "Test"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assert.notNull(MDC.get("time"), "Time empty");
        Assert.notNull(MDC.get("method"), "Method empty");
        Assert.isTrue(MDC.get("method").equalsIgnoreCase("PUT"), "Method invalid");
    }

}
