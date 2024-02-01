package com.apoorva.UserAssignment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import com.apoorva.UserAssignment.CustomersUtils;
import com.apoorva.UserAssignment.domain.CustomerEntity;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void testFindByFileNameReturnExpectedCustomer() {
		CustomerEntity customerA = CustomersUtils.createTestCustomerEntityA();
		customerRepository.save(customerA);
		List<CustomerEntity> customers = customerRepository.findByFilename(customerA.getFileName());
		assertEquals(customerA, customers.get(0));

	}

	@Test
	void testFindByFileNameReturnExpectedNumberOfCustomers() {
		CustomerEntity customerA = CustomersUtils.createTestCustomerEntityA();
		CustomerEntity customerB = CustomersUtils.createTestCustomerEntityB();
		customerRepository.save(customerA);
		customerRepository.save(customerB);
		List<CustomerEntity> customers = customerRepository.findByFilename("first_batch");
		assertEquals(2, customers.size());
	}

	@Test
	void testDeleteByFilenameIsDeletingCutomers() {
		CustomerEntity customerA = CustomersUtils.createTestCustomerEntityA();
		CustomerEntity customerB = CustomersUtils.createTestCustomerEntityB();
		customerRepository.save(customerA);
		customerRepository.save(customerB);
		customerRepository.deleteByFilename("first_batch");
		List<CustomerEntity> customers = customerRepository.findByFilename("first_batch");
		assertEquals(0, customers.size());
	}

	@Test
	void testWetherDeleteByFilenameIsOnlyDeletingWhoHaveFileName() {
		CustomerEntity customerA = CustomersUtils.createTestCustomerEntityA();
		CustomerEntity customerC = CustomersUtils.createTestCustomerEntityC();
		customerRepository.save(customerA);
		customerRepository.save(customerC);
		customerRepository.deleteByFilename("first_batch");
		List<CustomerEntity> customers = customerRepository.findByFilename("second_batch");
		assertEquals(1, customers.size());
	}
}
