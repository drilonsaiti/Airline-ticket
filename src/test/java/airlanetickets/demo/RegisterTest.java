package airlanetickets.demo;

import airlanetickets.model.Agency;
import airlanetickets.model.enumerations.Role;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RegisterTest {
    @Autowired
    MockMvc mockMvc;


    @Test
    @WithMockUser()
    public void testGetRegisterPage() throws Exception {
        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.get("/register");


        this.mockMvc.perform(registerRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","register"))
                .andExpect(view().name("master-template"));
    }


    @Test
    public void testCreateUser() throws Exception {
        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.post("/register");


        mockMvc.perform(registerRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username","userTest")
                        .param("password","userTest")
                        .param("repeatedPassword","userTest")
                        .param("name","userTest")
                        .param("surname","userTest")
                        .param("email","userTest@test.com")
                        .param("role", Role.ROLE_USER.toString())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void testCreateAdminUser() throws Exception {
        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.post("/register");


        mockMvc.perform(registerRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username","adminTest")
                        .param("password","adminTest")
                        .param("repeatedPassword","adminTest")
                        .param("name","adminTest")
                        .param("surname","adminTest")
                        .param("email","adminTest@test.com")
                        .param("role", Role.ROLE_ADMIN.toString())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void testCreateUserInvalidArguments() throws Exception {
        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.post("/register");


        mockMvc.perform(registerRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password","userTest")
                        .param("repeatedPassword","userTest")
                        .param("name","userTest")
                        .param("surname","userTest")
                        .param("email","user2user.com")
                        .param("role", Role.ROLE_USER.toString())

                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateUserUsernameExists() throws Exception {
        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.post("/register");


        mockMvc.perform(registerRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username","user2")
                        .param("password","userTest")
                        .param("repeatedPassword","userTest")
                        .param("name","userTest")
                        .param("surname","userTest")
                        .param("email","userTest@test.com")
                        .param("role", Role.ROLE_USER.toString())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/register?error=User with username: user2 already exists"));
    }
    @Test
    public void testCreateUserEmailExists() throws Exception {
        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.post("/register");


        mockMvc.perform(registerRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username","userTest")
                        .param("password","userTest")
                        .param("repeatedPassword","userTest")
                        .param("name","userTest")
                        .param("surname","userTest")
                        .param("email","user2@user.com")
                        .param("role", Role.ROLE_USER.toString())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/register?error=User with email: user2@user.com already exists"));
    }

    @Test
    public void testCreateUserInvalidEmail() throws Exception {
        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.post("/register");


        mockMvc.perform(registerRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username","userTest")
                        .param("password","userTest")
                        .param("repeatedPassword","userTest")
                        .param("name","userTest")
                        .param("surname","userTest")
                        .param("email","user2user.com")
                        .param("role", Role.ROLE_USER.toString())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/register?error=Your email need to be like example@email.com"));
    }

    @Test
    public void testCreateUserPasswordDoNotMatch() throws Exception {
        MockHttpServletRequestBuilder registerRequest = MockMvcRequestBuilders.post("/register");


        mockMvc.perform(registerRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username","userTest")
                        .param("password","userTest123")
                        .param("repeatedPassword","userTest")
                        .param("name","userTest")
                        .param("surname","userTest")
                        .param("email","userTest@user.com")
                        .param("role", Role.ROLE_USER.toString())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/register?error=Passwords do not match exception."));
    }

}
