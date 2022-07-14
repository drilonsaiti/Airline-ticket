package airlanetickets.demo;

import airlanetickets.model.Flight;
import airlanetickets.model.User;
import airlanetickets.model.enumerations.Role;
import airlanetickets.service.AuthService;
import airlanetickets.service.FlightService;
import airlanetickets.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class ProfileTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;



    @Test
    public void testShowProfile() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.get("/profile")
                .with(SecurityMockMvcRequestPostProcessors.user("userTest").roles("USER").password("userTest"));


        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","profile"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testShowProfiless() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.get("/ticket-cart")
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));


        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","ticket-cart"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testShowProfileNoAuthorize() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.get("/profile");


        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testShowProfileSettings() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.get("/profile/settings")
                .with(SecurityMockMvcRequestPostProcessors.user("userTest").roles("USER").password("userTest"));


        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","settings"))
                .andExpect(view().name("master-template"));
    }
    @Test
    public void testShowProfileSettingsNoAuthorize() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.get("/profile/settings");


        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.post("/profile/{username}","user")
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));
        User user = this.userService.findByUsername("user");

        this.mockMvc.perform(profileRequest.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("usernameOfUser",user.getUsername())
                        .param("nameOfUser","User2024")
                        .param("surnameOfUser",user.getSurname())
                        .param("emailOfUser",user.getEmail()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile"));
        user = this.userService.findByUsername("user");
        Assert.assertEquals("User2024",user.getName());
    }
    @Test
    public void testUpdateProfileNoAuthorize() throws Exception {
        MockHttpServletRequestBuilder profileRequest = MockMvcRequestBuilders.post("/profile/{username}","user");

        this.mockMvc.perform(profileRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }


    @Test
    public void testDeleteProfile() throws Exception {


        User user = this.userService.register("test","test","test","test","test","test@test.com", Role.ROLE_USER);
        Assert.assertEquals("test",user.getName());

        MockHttpServletRequestBuilder profileDeleteRequets = MockMvcRequestBuilders
                .post("/user/delete/{username}","test")
                .with(SecurityMockMvcRequestPostProcessors.user("test").roles("USER").password("test"));

        this.mockMvc.perform(profileDeleteRequets)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
        Throwable exp = Assert.assertThrows(UsernameNotFoundException.class, () -> {
            this.userService.findByUsername("test");
        });
        System.out.println("TEST message " + exp.getMessage());
        Assert.assertEquals("test",exp.getMessage());
    }

    @Test
    public void testNoAuthorizeProfileDelete() throws Exception {
        User user = this.userService.register("test2","test","test","test","test","test2@test.com", Role.ROLE_USER);
        Assert.assertEquals("test",user.getName());

        MockHttpServletRequestBuilder profileDeleteRequets = MockMvcRequestBuilders
                .post("/user/delete/{username}","test");
        this.mockMvc.perform(profileDeleteRequets)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
        this.authService.delete("test2");
    }

}
