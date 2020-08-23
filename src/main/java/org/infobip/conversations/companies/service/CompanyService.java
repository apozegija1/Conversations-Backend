package org.infobip.conversations.companies.service;

import org.infobip.conversations.companies.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyService {
   private final CompanyRepository companyRepository;

   public CompanyService(CompanyRepository companyRepository) {
      this.companyRepository = companyRepository;
   }

}
