package com.flightapp.admin.controller;

import com.flightapp.admin.entity.Admin;
import com.flightapp.admin.model.AddFlightRequest;
import com.flightapp.admin.model.RegisterAirlineRequest;
import com.flightapp.admin.model.RegisterAirlineResponse;
import com.flightapp.admin.service.AdminService;
import com.flightapp.admin.service.FlightService;
import com.flightapp.admin.service.JwtUserDetailsService;
import com.flightapp.admin.util.JwtTokenUtil;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class AdminControllerTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private AdminService mockAdminService;
    @Mock
    private FlightService mockFlightService;
    @Mock
    private AuthenticationManager mockAuthenticationManager;
    @Mock
    private JwtTokenUtil mockJwtTokenUtil;
    @Mock
    private JwtUserDetailsService mockUserDetailsService;
    @Mock
    private KafkaTemplate<String, List<AddFlightRequest>> mockKafkaTemplate;

    @Test
    void testCreateAuthenticationToken() throws Exception {
        // Setup
        when(mockAuthenticationManager.authenticate(null)).thenReturn(null);
        when(mockUserDetailsService.loadUserByUsername("username")).thenReturn(null);
        when(mockJwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn("jwttoken");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1.0/admin/authenticate")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockAuthenticationManager).authenticate(null);
    }

    @Test
    void testCreateAuthenticationToken_AuthenticationManagerThrowsAuthenticationException() throws Exception {
        // Setup
        when(mockAuthenticationManager.authenticate(null)).thenThrow(AuthenticationException.class);
        when(mockUserDetailsService.loadUserByUsername("username")).thenReturn(null);
        when(mockJwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn("jwttoken");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1.0/admin/authenticate")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testCreateAuthenticationToken_JwtUserDetailsServiceThrowsUsernameNotFoundException() throws Exception {
        // Setup
        when(mockAuthenticationManager.authenticate(null)).thenReturn(null);
        when(mockUserDetailsService.loadUserByUsername("username")).thenThrow(UsernameNotFoundException.class);
        when(mockJwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn("jwttoken");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1.0/admin/authenticate")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockAuthenticationManager).authenticate(null);
    }

    @Test
    void testGetStatus() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1.0/admin/status")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testAddAdmin() throws Exception {
        // Setup
        when(mockAdminService.addAdmin(new Admin())).thenReturn(false);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1.0/admin/addAdmin")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockAdminService).addAdmin(new Admin());
    }

    @Test
    void testRegisterAirline() throws Exception {
        // Setup
        // Configure FlightService.registerNewAirline(...).
        final RegisterAirlineResponse registerAirlineResponse = new RegisterAirlineResponse();
        registerAirlineResponse.setAirlineId(0);
        registerAirlineResponse.setName("name");
        registerAirlineResponse.setContactNo("contactNo");
        registerAirlineResponse.setAddress("address");
        registerAirlineResponse.setDescription("description");
        final ResponseEntity<RegisterAirlineResponse> registerAirlineResponseEntity = new ResponseEntity<>(
                registerAirlineResponse, HttpStatus.OK);
        when(mockFlightService.registerNewAirline(new RegisterAirlineRequest()))
                .thenReturn(registerAirlineResponseEntity);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1.0/admin/registerAirline")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testAddFlights() throws Exception {
        // Setup
        // Configure KafkaTemplate.send(...).
        final SendResult<String, List<AddFlightRequest>> stringListSendResult = new SendResult<>(
                new ProducerRecord<>("topic",
                        List.of(new AddFlightRequest(0, "name", "source", "destination", "departure", "arrival", 0,
                                "seats", 0))), new RecordMetadata(new TopicPartition("topic", 0), 0L, 0, 0L, 0, 0));
        final SettableListenableFuture<SendResult<String, List<AddFlightRequest>>> sendResultListenableFuture = new SettableListenableFuture<>();
        sendResultListenableFuture.set(stringListSendResult);
        when(mockKafkaTemplate.send("flight-schedules",
                List.of(new AddFlightRequest(0, "name", "source", "destination", "departure", "arrival", 0, "seats",
                        0)))).thenReturn(sendResultListenableFuture);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1.0/admin/addFlightSchedule")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockKafkaTemplate).send("flight-schedules",
                List.of(new AddFlightRequest(0, "name", "source", "destination", "departure", "arrival", 0, "seats",
                        0)));
    }

    @Test
    void testAddFlights_KafkaTemplateReturnsFailure() throws Exception {
        // Setup
        // Configure KafkaTemplate.send(...).
        final SettableListenableFuture<SendResult<String, List<AddFlightRequest>>> sendResultListenableFuture = new SettableListenableFuture<>();
        sendResultListenableFuture.setException(new Exception("message"));
        when(mockKafkaTemplate.send("flight-schedules",
                List.of(new AddFlightRequest(0, "name", "source", "destination", "departure", "arrival", 0, "seats",
                        0)))).thenReturn(sendResultListenableFuture);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1.0/admin/addFlightSchedule")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
        verify(mockKafkaTemplate).send("flight-schedules",
                List.of(new AddFlightRequest(0, "name", "source", "destination", "departure", "arrival", 0, "seats",
                        0)));
    }
}
