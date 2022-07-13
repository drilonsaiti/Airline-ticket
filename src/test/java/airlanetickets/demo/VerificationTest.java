package airlanetickets.demo;


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
public class VerificationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testShowVerification() throws Exception {
        MockHttpServletRequestBuilder verificationRequest = MockMvcRequestBuilders.get("/verification")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));


        this.mockMvc.perform(verificationRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","verificationSearch"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testShowNoAuthorizeContact() throws Exception {
        MockHttpServletRequestBuilder verificationRequest = MockMvcRequestBuilders.get("/verification");


        this.mockMvc.perform(verificationRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testCheckBoardingPass() throws Exception {
        MockHttpServletRequestBuilder verificationRequest = MockMvcRequestBuilders.get("/verification/search")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));


        this.mockMvc.perform(verificationRequest.param("idOrder","1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

}
