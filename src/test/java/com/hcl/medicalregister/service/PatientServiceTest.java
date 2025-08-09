
package com.hcl.medicalregister.service;

import com.hcl.medicalregister.domain.Patient;
import com.hcl.medicalregister.repository.IPatientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Mock
    private IPatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    public PatientServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient found = patientService.findById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    public void testDelete() {
        Long id = 2L;
        doNothing().when(patientRepository).deleteById(id);
        patientService.delete(id);
        verify(patientRepository, times(1)).deleteById(id);
    }
}
