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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testShowLoginPage() throws Exception {
        MockHttpServletRequestBuilder testRequest = MockMvcRequestBuilders.get("/login");


        this.mockMvc.perform(testRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","login"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testLoginWithAdmin() throws Exception {
        MockHttpServletRequestBuilder testRequest = MockMvcRequestBuilders.post("/login")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        this.mockMvc.perform(testRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testLoginWithUser() throws Exception {
        MockHttpServletRequestBuilder testRequest = MockMvcRequestBuilders.post("/login")
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));

        this.mockMvc.perform(testRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testLoginError() throws Exception {
        MockHttpServletRequestBuilder testRequest = MockMvcRequestBuilders.post("/login");

        this.mockMvc.perform(testRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/login?error=BadCredentials"));
    }

    @Test
    public void testLoginInvalidUsername() throws Exception {
        MockHttpServletRequestBuilder testRequest = MockMvcRequestBuilders.post("/login")
                .with(SecurityMockMvcRequestPostProcessors.user("user123"));

        this.mockMvc.perform(testRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/login?error=BadCredentials"));
    }


    @Test
    public void testLoginInvalidPassword() throws Exception {
        MockHttpServletRequestBuilder testRequest = MockMvcRequestBuilders.post("/login")
                .with(SecurityMockMvcRequestPostProcessors.user("user").password("1234565"));

        this.mockMvc.perform(testRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/login?error=BadCredentials"));
    }

    @Test
    public void testLoginEmptyPassword() throws Exception {
        MockHttpServletRequestBuilder testRequest = MockMvcRequestBuilders.post("/login")
                .with(SecurityMockMvcRequestPostProcessors.user("user"));

        this.mockMvc.perform(testRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/login?error=BadCredentials"));
    }




}
