package com.apoorva.UserAssignment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apoorva.UserAssignment.domain.CustomerEntity;
import com.apoorva.UserAssignment.exceptions.BadRequestException;
import com.apoorva.UserAssignment.repository.CustomerRepository;
import com.apoorva.UserAssignment.service.CustomerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	/*
	 * Method to get all the data from database.
	 * @return list of customer
	 */
	public List<CustomerEntity> getAll() {
		return customerRepository.findAll();
	}

	/*
	 * Method to insert a single customer in database
	 * @param customer
	 */
	public void addCustomer(CustomerEntity customer) {
		//Checking if phoneNumber already exist in Database.
		Boolean duplicatePhoneNumber = customerRepository.findByPhoneNumber(customer.getPhoneNumber()).isPresent();
		if (duplicatePhoneNumber) {
			log.error("PhoneNumber already registered.");
			throw new BadRequestException("PhoneNumber " + customer.getPhoneNumber() + " taken");
		}
		// Adding customer to database.
		customerRepository.save(customer);
	}

	/*
	 * Method to delete customer by Id 
	 * @param id
	 * @return deleted customer entity
	 */
	public Optional<CustomerEntity> deleteById(Long id) {
		Optional<CustomerEntity> optionalCustomer = customerRepository.findById(id);
		if (optionalCustomer.isPresent()) {
			customerRepository.deleteById(id);
		}
		return optionalCustomer;
	}

	/*
	 * Method to delete all the customers from a file.
	 * @param filename
	 * @return count of deleted customers
	 */
	public int deleteByFilename(String filename) {
		List<CustomerEntity> customers = customerRepository.findByFilename(filename);
		customerRepository.deleteByFilename(filename);
		return customers.size();
	}

}
