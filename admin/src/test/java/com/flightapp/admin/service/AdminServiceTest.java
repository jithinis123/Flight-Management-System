package com.flightapp.admin.service;

import com.flightapp.admin.entity.Admin;
import com.flightapp.admin.exception.CustomException;
import com.flightapp.admin.repository.AdminRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceTest {

    @Mock
    AdminRepo repo;

    @InjectMocks
    private AdminService adminServiceUnderTest;

    @Test
    void testFetchAdminDetails() {
        
        final Admin expectedResult = new Admin();
        expectedResult.setUsername("username");
        expectedResult.setPassword("password");

        final Admin admin1 = new Admin();
        admin1.setUsername("username");
        admin1.setPassword("password");
        final Optional<Admin> admin = Optional.of(admin1);
        when(repo.findByUsername("username")).thenReturn(admin);

        final Admin result = adminServiceUnderTest.fetchAdminDetails("username");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFetchAdminDetails_FetchAdminDetails_throwsException() {
        
        final Admin expectedResult = new Admin();
        expectedResult.setUsername("username");
        expectedResult.setPassword("password");

        when(repo.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(CustomException.class, ()->adminServiceUnderTest.fetchAdminDetails("username"));

    }

    @Test
    void testAddAdmin() {

        AdminService adminServiceSpy = spy(adminServiceUnderTest);
        final Admin admin = new Admin();
        admin.setUsername("username");
        admin.setPassword("hash");

        doReturn("hash").when(adminServiceSpy).hashPassword(any());
        when(adminServiceSpy.repo.save(admin)).thenReturn(admin);

        assertTrue(adminServiceSpy.addAdmin(admin));

        verify(adminServiceSpy.repo).save(admin);
    }

    @Test
    void testHashPassword() {
        assertThat(adminServiceUnderTest.hashPassword("password")).hasSize(60);
    }
}
