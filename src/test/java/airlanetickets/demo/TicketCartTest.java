package airlanetickets.demo;

import airlanetickets.model.Agency;
import airlanetickets.service.AgencyService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TicketCartTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AgencyService agencyService;




    @Test
    public void testShowTicketCart() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.get("/ticket-cart")
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));


        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","ticket-cart"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testShowTicketCartDownload() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.get("/ticket-cart/{id}/download",1)
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));


        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("download"));
    }

    @Test
    public void testShowTicketCartCancaled() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.post("/ticket-cart/{id}/canceled",9)
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));


        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ticket-cart"));
    }

    @Test
    public void testShowTicketCartClear() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.post("/ticket-cart/clear")
                .with(SecurityMockMvcRequestPostProcessors.user("user2").roles("USER").password("user2"));


        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ticket-cart"));
    }




}
