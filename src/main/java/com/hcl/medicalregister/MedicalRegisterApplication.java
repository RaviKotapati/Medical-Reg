package com.hcl.medicalregister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

@EntityScan("com.hcl.medicalregister.domain")
@EnableLdapRepositories(basePackages = "com.hcl.medicalregister.repository")
@SpringBootApplication
public class MedicalRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalRegisterApplication.class, args);
    }

}
