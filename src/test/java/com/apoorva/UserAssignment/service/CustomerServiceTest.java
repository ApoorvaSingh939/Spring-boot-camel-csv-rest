package com.apoorva.UserAssignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.apoorva.UserAssignment.CustomersUtils;
import com.apoorva.UserAssignment.domain.CustomerEntity;
import com.apoorva.UserAssignment.exceptions.BadRequestException;
import com.apoorva.UserAssignment.repository.CustomerRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;
	private CustomerService customerServiceTest;

	@BeforeEach
	void setup() {
		customerServiceTest = new CustomerService(customerRepository);

	}

	@Test
	void canGetAllCustomers() {

		customerServiceTest.getAll();
		verify(customerRepository).findAll();

	}

	@Test
	void canAddCustomers() {
		CustomerEntity customer = CustomersUtils.createTestCustomerEntityA();

		// when
		customerServiceTest.addCustomer(customer);

		ArgumentCaptor<CustomerEntity> customerArgumentCaptor = ArgumentCaptor.forClass(CustomerEntity.class);

		verify(customerRepository).save(customerArgumentCaptor.capture());

		CustomerEntity capturedCustomer = customerArgumentCaptor.getValue();
		assertThat(capturedCustomer).isEqualTo(customer);

	}

	@Test
	void willThrowErrorWhenPhoneNumberIsTaken() {

		CustomerEntity customer = CustomersUtils.createTestCustomerEntityA();

		// make sure that the findByPhoneNumber return a customer
		given(customerRepository.findByPhoneNumber(anyString())).willReturn(Optional.ofNullable(customer));

		assertThatThrownBy(() -> customerServiceTest.addCustomer(customer)).isInstanceOf(BadRequestException.class)
				.hasMessageContaining("PhoneNumber " + customer.getPhoneNumber() + " taken");

		verify(customerRepository, never()).save(any());

	}

	@Test
	void canDeleteCustomersById() {

		CustomerEntity customer = CustomersUtils.createTestCustomerEntityA();
		long id = customer.getId();
		given(customerRepository.findById(id)).willReturn(Optional.ofNullable((customer)));

		customerServiceTest.deleteById(id);

		verify(customerRepository).deleteById(id);
	}

	@Test
	void canDeleteCustomersByIdIfTest() {

		CustomerEntity customer = CustomersUtils.createTestCustomerEntityA();
		long id = customer.getId();
		given(customerRepository.findById(id)).willReturn(Optional.ofNullable((null)));

		customerServiceTest.deleteById(id);

		verify(customerRepository, times(0)).deleteById(id);
	}

	@Test
	void canDeleteCustomersByFileName() {

		CustomerEntity customer = CustomersUtils.createTestCustomerEntityA();
		String filename = customer.getFileName();

		int size = customerServiceTest.deleteByFilename(filename);

		verify(customerRepository).deleteByFilename(filename);

		assertThat(size).isEqualTo(0);

	}

}
