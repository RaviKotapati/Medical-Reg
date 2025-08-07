/*
package com.hcl.medicalregister.bean;

import com.hcl.medicalregister.domain.Patient;
import com.hcl.medicalregister.service.PatientService;
import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class PatientBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Patient patient = new Patient();
    private List<Patient> patients;

    @Inject
    private PatientService patientService;

    @PostConstruct
    public void init() {
        patients = patientService.findAll();
    }

    public String addPatient() {
        patientService.save(patient);
        patients = patientService.findAll();
        patient = new Patient(); // reset form
        return "patientInfo.xhtml?faces-redirect=true";
    }

    public void editPatient(Patient p) {
        System.out.println("PATIRN::&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ");
        System.out.println("PATIRN:: "+p);
        this.patient = p;

    }

    public String updatePatient() {
        patientService.save(patient);
        patients = patientService.findAll();
        patient = new Patient();
        return "patientInfo.xhtml?faces-redirect=true";
    }
    public void save() {
        */
/*patientService.save(patient);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Patient saved"));
        patient = new Patient(); // Reset
        patients = patientService.findAll(); // Refresh*//*

    }
    public void deletePatient(Patient patient) {
        patientService.delete(patient.getId());
        patients = patientService.findAll();
    }
    public void deletePatient(Long id) {
        patientService.delete(id);
        patients = patientService.findAll();
    }

    // Getters and Setters
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
*/
