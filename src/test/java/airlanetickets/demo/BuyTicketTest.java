package airlanetickets.demo;


import airlanetickets.model.Flight;
import airlanetickets.model.enumerations.ClassesType;
import airlanetickets.service.FlightService;
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
public class BuyTicketTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FlightService flightService;

    @Test
    public void testBuyTickets() throws Exception {
        //Include PaymentController,ReservationController


        String id = "21";
        String idReservation = "4";
        MockHttpServletRequestBuilder contactRequest = MockMvcRequestBuilders.post("/reservation/setId")
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));


        this.mockMvc.perform(contactRequest.param("idFlight",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection());

        MockHttpServletRequestBuilder getReservationRequest = MockMvcRequestBuilders.get("/reservation")
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));

        Flight flight = this.flightService.findById(Long.parseLong(id));
        this.mockMvc.perform(getReservationRequest.param("idFlight",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","reservation"))
                .andExpect(view().name("master-template"));

        MockHttpServletRequestBuilder postReservationRequest = MockMvcRequestBuilders.post("/reservation")
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));

        this.mockMvc.perform(postReservationRequest
                        .param("nameOfCust","Drilon")
                        .param("surOfCust","Saiti")
                        .param("numofPass","G123456")
                        .param("countryCode","389")
                        .param("numberOfPhone","123123")
                        .param("type", ClassesType.ECONOMY_CLASS.toString())

                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection());

        MockHttpServletRequestBuilder paymentRequest = MockMvcRequestBuilders.get("/payment")
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));


        this.mockMvc.perform(paymentRequest
                        .param("idFlights",id)
                        .param("idReservation",idReservation)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent","payment"))
                .andExpect(view().name("master-template"));

        MockHttpServletRequestBuilder postPaymentRequest = MockMvcRequestBuilders.post("/payment")
                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER").password("user"));

        this.mockMvc.perform(postPaymentRequest
                        .param("idFlights",id)
                        .param("idReservations",idReservation)
                        .param("fullname","DRILON SAITI")
                        .param("cardNumber","111111111111")
                        .param("ccv2","111")
                        .param("monthfValid","02")
                        .param("yearOfValid","2026")

                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(redirectedUrl("/ticket-cart"));
    }



}
