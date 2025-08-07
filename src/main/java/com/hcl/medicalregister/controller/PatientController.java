package com.hcl.medicalregister.controller;

import com.hcl.medicalregister.domain.Patient;
import com.hcl.medicalregister.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientService service;

    @GetMapping("/home")
    public String home() {
        System.out.println("**********************************************");
        return "login.xhtml";
    }




    @GetMapping("/{id}")
    public Patient get(@PathVariable Long id) {
        return service.findById(id);
    }

	@PostMapping("/patient/save")
	public ResponseEntity<?> create(@RequestBody Patient p, HttpServletRequest request) {
		System.out.println(" SAVE    *******************");

		 service.save(p);
        return ResponseEntity.ok(p);
	}
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        System.out.println(" getAllPatients**    getAllPatients ****");
        List<Patient> patients = service.findAll();
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    public Patient update(@RequestBody Patient p, @PathVariable Long id) {
        p.setId(id);
        return service.save(p);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}