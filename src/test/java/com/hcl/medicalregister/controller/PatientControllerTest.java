package com.hcl.medicalregister.controller;

import com.hcl.medicalregister.domain.Patient;
import com.hcl.medicalregister.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerUnitTest {

    @Mock
    private PatientService service;

    @InjectMocks
    private PatientController controller;

    private Patient p1;

    @BeforeEach
    void setUp() {
        p1 = new Patient();

    }


    @Test
    void home_returnsLoginXhtml() {
        String view = controller.home();
        assertThat(view).isEqualTo("login");
    }


    @Test
    void get_existingId_returnsOkPatient() {
        when(service.findById(1L)).thenReturn(p1);

        ResponseEntity<?> resp = controller.get(1L);

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(p1);
        verify(service).findById(1L);
    }

    @Test
    void get_missingId_returns404() {
        when(service.findById(999L)).thenThrow(new NoSuchElementException("not found"));

        ResponseEntity<?> resp = controller.get(999L);

        assertThat(resp.getStatusCodeValue()).isEqualTo(404);
        assertThat(resp.getBody()).asString().contains("Patient not found with ID: 999");
        verify(service).findById(999L);
    }


    @Test
    void create_success_returnsOkSavedPatient() {
        when(service.save(any(Patient.class))).thenReturn(p1);

        ResponseEntity<?> resp = controller.create(new Patient());

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(p1);
        verify(service).save(any(Patient.class));
    }

    @Test
    void create_serviceThrows_returns500() {
        when(service.save(any(Patient.class))).thenThrow(new RuntimeException("db down"));

        ResponseEntity<?> resp = controller.create(new Patient());

        assertThat(resp.getStatusCodeValue()).isEqualTo(500);
        assertThat(resp.getBody()).asString().contains("Error saving patient: db down");
        verify(service).save(any(Patient.class));
    }


    @Test
    void getAllPatients_success_returnsOkList() {
        List<Patient> list = Arrays.asList(p1, new Patient());
        when(service.findAll()).thenReturn(list);

        ResponseEntity<?> resp = controller.getAllPatients();

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).isSameAs(list);
        verify(service).findAll();
    }

    @Test
    void getAllPatients_serviceThrows_returns500() {
        when(service.findAll()).thenThrow(new RuntimeException("query failed"));

        ResponseEntity<?> resp = controller.getAllPatients();

        assertThat(resp.getStatusCodeValue()).isEqualTo(500);
        assertThat(resp.getBody()).asString().contains("Error retrieving patients: query failed");
        verify(service).findAll();
    }


    @Test
    void delete_existingId_returnsOk() {
        doNothing().when(service).delete(1L);

        ResponseEntity<?> resp = controller.delete(1L);

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).isNull();
        verify(service).delete(1L);
    }

    @Test
    void delete_missingId_returns404() {
        doThrow(new NoSuchElementException("no such id")).when(service).delete(999L);

        ResponseEntity<?> resp = controller.delete(999L);

        assertThat(resp.getStatusCodeValue()).isEqualTo(404);
        assertThat(resp.getBody()).asString().contains("Patient not found for delete: 999");
        verify(service).delete(999L);
    }

    @Test
    void delete_serviceThrowsOther_returns500() {
        doThrow(new RuntimeException("constraint violation")).when(service).delete(2L);

        ResponseEntity<?> resp = controller.delete(2L);

        assertThat(resp.getStatusCodeValue()).isEqualTo(500);
        assertThat(resp.getBody()).asString().contains("Error deleting patient: constraint violation");
        verify(service).delete(2L);
    }
}
