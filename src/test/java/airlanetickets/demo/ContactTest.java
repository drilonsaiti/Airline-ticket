package airlanetickets.demo;


import airlanetickets.service.AirplaneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ContactTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testShowContact() throws Exception {
        MockHttpServletRequestBuilder contactRequest = MockMvcRequestBuilders.get("/contact")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));


        this.mockMvc.perform(contactRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","contact"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testShowNoAuthorizeContact() throws Exception {
        MockHttpServletRequestBuilder contactRequest = MockMvcRequestBuilders.get("/contact");


        this.mockMvc.perform(contactRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection());
    }
}
