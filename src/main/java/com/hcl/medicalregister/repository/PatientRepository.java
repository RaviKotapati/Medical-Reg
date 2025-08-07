package com.hcl.medicalregister.repository;

import com.hcl.medicalregister.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {}