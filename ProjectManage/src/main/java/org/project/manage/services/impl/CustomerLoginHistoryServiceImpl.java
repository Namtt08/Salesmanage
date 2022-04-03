package org.project.manage.services.impl;

import org.project.manage.entities.CustomerLoginHistory;
import org.project.manage.repository.CustomerLoginHistoryRepository;
import org.project.manage.services.CustomerLoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerLoginHistoryServiceImpl implements CustomerLoginHistoryService {

	@Autowired
	private CustomerLoginHistoryRepository customerLoginHistoryRepository;
	
	@Override
	public CustomerLoginHistory save(CustomerLoginHistory userCustomer) {
		return customerLoginHistoryRepository.save(userCustomer);
	}

}
