package com.hcl.medicalregister.service;

import com.hcl.medicalregister.domain.Patient;
import com.hcl.medicalregister.repository.IPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class PatientService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Autowired
    private IPatientRepository repository;

    public List<Patient> findAll() {
        return repository.findAll();
    }

    public Patient findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Patient save(Patient p) {
        return repository.save(p);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}