package airlanetickets.demo;

import airlanetickets.model.Agency;
import airlanetickets.model.Airplane;
import airlanetickets.model.Flight;
import airlanetickets.model.exceptions.InvalidFlightIdException;
import airlanetickets.service.AgencyService;
import airlanetickets.service.AirplaneService;
import airlanetickets.service.FlightService;
import airlanetickets.service.UserService;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FlightTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    FlightService flightService;



    @Test
    public void testGetFlights() throws Exception {
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.get("/flights");


        this.mockMvc.perform(flightsRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","flights"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testSearchToDestinationFlights() throws Exception {
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.get("/flights/page/{pageNo}",1);


        this.mockMvc.perform(flightsRequest
                        .param("toSearch","Zurich"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalItems",Long.parseLong("1")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","flights"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testSearchFromDestinationFlights() throws Exception {
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.get("/flights/page/{pageNo}",1);


        this.mockMvc.perform(flightsRequest
                        .param("fromSearch","Munich"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalItems",Long.parseLong("1")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","flights"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testSearchTimeDestinationFlights() throws Exception {
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.get("/flights/page/{pageNo}",1);


        this.mockMvc.perform(flightsRequest
                        .param("deptSearch","2022-07-19"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalItems",Long.parseLong("2")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","flights"))
                .andExpect(view().name("master-template"));
    }

    @Test
    public void testShowEditFlight() throws Exception {
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.get("/flights/8/edit")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));


        this.mockMvc.perform(flightsRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("flight"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("agencies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("airplanes"))

                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","add-flights"))
                .andExpect(view().name("master-template"));
    }
    @Test
    public void testShowEditNoAuthorizeFlight() throws Exception {
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.get("/flights/8/edit");


        this.mockMvc.perform(flightsRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }
    @Test
    public void testCreateFlights() throws Exception {
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.post("/flights")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        mockMvc.perform(flightsRequest
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("departureFrom","Skopje")
                .param("departureTo","Munich")
                .param("departureTime","20-07-2022 16:00")
                .param("arrivalTime","20-07-2022 18:00")
                .param("agency","1")
                .param("airplane","1")
                .param("duration","2hours")
                .param("price","200")
                .param("seats","200")
                        .sessionAttr("flight",new Flight())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/flights"));
    }

    @Test
    public void testCreateFailedFlights() throws Exception {
        //CLIENT_ERROR - because one of params is empty
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.post("/flights")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));


        mockMvc.perform(flightsRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("departureFrom","Skopje")
                        .param("departureTo","Munich")
                        .param("departureTime","20-07-2022 16:00")
                        .param("arrivalTime","20-07-2022 18:00")
                        .param("agency","1")
                        .param("airplane","1")
                        .param("duration","2hours")
                        .param("price","200")

                        .sessionAttr("flight",new Flight())
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateNoAuthorizeFlights() throws Exception {
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.post("/flights");

        mockMvc.perform(flightsRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("departureFrom","Skopje")
                        .param("departureTo","Munich")
                        .param("departureTime","20-07-2022 16:00")
                        .param("arrivalTime","20-07-2022 18:00")
                        .param("agency","1")
                        .param("airplane","1")
                        .param("duration","2hours")
                        .param("price","200")
                        .param("seats","200")
                        .sessionAttr("flight",new Flight())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }



    @Test
    public void testUpdateFlights() throws Exception {
        long id = 8;
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.post("/flights/{id}",id)
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        mockMvc.perform(flightsRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("departureFrom","Skopje")
                        .param("departureTo","Madrid")
                        .param("departureTime","20-07-2022 16:00")
                        .param("arrivalTime","20-07-2022 18:00")
                        .param("agency","1")
                        .param("airplane","1")
                        .param("duration","2hours")
                        .param("price","200")
                        .param("seats","200")


                        .sessionAttr("flight",new Flight())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/flights"));
        Flight flight = flightService.findById(id);
        Assert.assertEquals("Madrid",flight.getToLocation());
    }

    @Test
    public void testNoAuthorizeUpdateFlights() throws Exception {
        long id = 8;
        MockHttpServletRequestBuilder flightsRequest = MockMvcRequestBuilders.post("/flights/{id}",id);

        mockMvc.perform(flightsRequest
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("departureFrom","Skopje")
                        .param("departureTo","Madrid")
                        .param("departureTime","20-07-2022 16:00")
                        .param("arrivalTime","20-07-2022 18:00")
                        .param("agency","1")
                        .param("airplane","1")
                        .param("duration","2hours")
                        .param("price","200")
                        .param("seats","200")


                        .sessionAttr("flight",new Flight())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));

    }

    @Test
    public void testDeleteFlight() throws Exception {

        long agency = 1;
        long airplane = 1;
        Flight flight = this.flightService.create("test","test","18-07-2022 16:00","18-07-2022 16:00",agency,airplane,"2hours",200,200);
        MockHttpServletRequestBuilder flightDeleteRequets = MockMvcRequestBuilders
                .post("/flights/" + flight.getId()+"/delete")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN").password("admin"));

        this.mockMvc.perform(flightDeleteRequets)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/flights"));
    }
    @Test
    public void testNoAuthorizeDeleteFlight() throws Exception {

        long agency = 1;
        long airplane = 1;
        Flight flight = this.flightService.create("test","test","18-07-2022 16:00","18-07-2022 16:00",agency,airplane,"2hours",200,200);
        MockHttpServletRequestBuilder flightDeleteRequets = MockMvcRequestBuilders
                .post("/flights/" + flight.getId()+"/delete");

        this.mockMvc.perform(flightDeleteRequets)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

}
