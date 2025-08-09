package com.hcl.medicalregister.controller;

import com.hcl.medicalregister.domain.Patient;
import com.hcl.medicalregister.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class PatientController {

    @Autowired
    private PatientService service;

    @GetMapping("/home")
    public String home() {
        System.out.println("Home page loaded");
        return "login";
    }

    @GetMapping("/patient/Details")
    public String patientDetails() {
        System.out.println("Details loaded");
        return "patientInfo";
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            Patient patient = service.findById(id);
            return ResponseEntity.ok(patient);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found with ID: " + id);
        }
    }

    @PostMapping("/patient/save")
    public ResponseEntity<?> create(@RequestBody Patient patient) {
        try {
            Patient saved = service.save(patient);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving patient: " + e.getMessage());
        }
    }

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients() {
        try {
            List<Patient> patients = service.findAll();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving patients: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found for delete: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting patient: " + e.getMessage());
        }
    }
}
