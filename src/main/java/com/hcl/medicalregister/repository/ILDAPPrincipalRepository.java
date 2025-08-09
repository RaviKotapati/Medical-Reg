package com.hcl.medicalregister.repository;

import org.springframework.data.ldap.repository.LdapRepository;


import com.hcl.medicalregister.domain.ADPrincipal;


public interface ILDAPPrincipalRepository extends LdapRepository<ADPrincipal>{
	ADPrincipal findByCn(String cn);
	ADPrincipal findByCnAndPassword(String cn, String password);

}
