package airlanetickets.demo;

import airlanetickets.model.Agency;
import airlanetickets.model.Flight;
import airlanetickets.model.enumerations.Role;
import airlanetickets.service.AgencyService;
import org.junit.Assert;
import org.junit.Before;
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
public class AgencyTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AgencyService agencyService;




    @Test
    @WithMockUser()
    public void testGetAgencies() throws Exception {
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.get("/agencies")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));


        this.mockMvc.perform(agencyRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("agencies"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","agencies"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testShowEditAgencies() throws Exception {
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.get("/agencies/3/edit")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));


        this.mockMvc.perform(agencyRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("agency"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-agency"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testCreateAgencies() throws Exception {
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.post("/agencies")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        mockMvc.perform(agencyRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nameOfAgency","Fly Emirates")
                        .param("cityOfAgency","Hong Kong")
                        .param("countryOfAgency","UAE")
                        .param("yearOfCreated","1990")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/agencies"));
    }

    @Test
    public void testCreateFailedAgencies() throws Exception {
        //CLIENT_ERROR - because one of params is empty
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.post("/agencies")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        mockMvc.perform(agencyRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nameOfAgency","Fly Emirates")
                        .param("cityOfAgency","Dubai")
                        .param("countryOfAgency","UAE")
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdateAgencies() throws Exception {
        long id = 2;
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.post("/agencies/{id}",id)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        mockMvc.perform(agencyRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nameOfAgency","Fly Emirates")
                        .param("cityOfAgency","Dubai")
                        .param("countryOfAgency","UAE")
                        .param("yearOfCreated","1990")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/agencies"));
        Agency agency = agencyService.findById(id);
        Assert.assertEquals("Dubai",agency.getCity());
    }

    @Test
    public void testNoAuthorizeUpdateAgencies() throws Exception {
        long id = 2;
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders.post("/agencies/{id}",id);

        mockMvc.perform(agencyRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nameOfAgency","Fly Emirates")
                        .param("cityOfAgency","Dubai")
                        .param("countryOfAgency","UAE")
                        .param("yearOfCreated","1990")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));


    }

    @Test
    public void testDeleteAgency() throws Exception {

      long id = 5;
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders
                .post("/agencies/{id}/delete",id)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        this.mockMvc.perform(agencyRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/agencies"));
    }
    @Test
    public void testNoAuthorizeDeleteAgency() throws Exception {

        long id = 5;
        MockHttpServletRequestBuilder agencyRequest = MockMvcRequestBuilders
                .post("/agencies/{id}/delete",id);

        this.mockMvc.perform(agencyRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }


}
