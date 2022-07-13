package airlanetickets.demo;

import airlanetickets.model.Agency;
import airlanetickets.model.Airplane;
import airlanetickets.service.AirplaneService;
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
public class AirplaneTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AirplaneService airplaneService;




    @Test
    @WithMockUser()
    public void testGetAirplanes() throws Exception {
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.get("/airplanes")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));


        this.mockMvc.perform(agencyRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("airplanes"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","airplanes"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testShowEditAirplane() throws Exception {
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.get("/airplanes/3/edit")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));


        this.mockMvc.perform(agencyRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("plane"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-airplane"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testCreateAirplane() throws Exception {
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.post("/airplanes")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        mockMvc.perform(agencyRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nameOfAirplane","Plane 001")
                        .param("yearOfCreatedPlane","1990")
                        .param("totalSeatsPlane","250")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/airplanes"));
    }

    @Test
    public void testCreateFailedAirplane() throws Exception {
        //CLIENT_ERROR - because one of params is empty
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.post("/airplanes")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        mockMvc.perform(agencyRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nameOfAirplane","Plane 001")
                        .param("yearOfCreatedPlane","1990")
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdateAgencies() throws Exception {
        long id = 2;
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.post("/airplanes/{id}",id)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        mockMvc.perform(agencyRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nameOfAirplane","Plane 001")
                        .param("yearOfCreatedPlane","1990")
                        .param("totalSeatsPlane","250")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/airplanes"));
        Airplane airplane = airplaneService.findById(id);
        Assert.assertEquals("Plane 001",airplane.getNameOfAirplane());
    }

    @Test
    public void testNoAuthorizeUpdateAirplane() throws Exception {
        long id = 2;
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.post("/airplanes/{id}",id);

        mockMvc.perform(agencyRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nameOfAirplane","Plane 001")
                        .param("yearOfCreatedPlane","1990")
                        .param("totalSeatsPlane","250")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));


    }

    @Test
    public void testDeleteAirplane() throws Exception {

      long id = 5;
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders
                .post("/airplanes/{id}/delete",id)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        this.mockMvc.perform(agencyRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/airplanes"));
    }
    @Test
    public void testNoAuthorizeDeleteAirplane() throws Exception {

        long id = 5;
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders
                .post("/airplanes/{id}/delete",id);

        this.mockMvc.perform(agencyRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }


}
