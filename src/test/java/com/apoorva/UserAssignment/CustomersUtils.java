package com.apoorva.UserAssignment;

import com.apoorva.UserAssignment.domain.CustomerEntity;

public final class CustomersUtils {
	private CustomersUtils() {
	}

	public static CustomerEntity createTestCustomerEntityA() {

		return CustomerEntity.builder().id(1L).firstName("apoorva").lastName("singh")
				.phoneNumber("1111111111")
				.fileName("first_batch").build();
	}

	public static CustomerEntity createTestCustomerEntityB() {

		return CustomerEntity.builder().id(2L).firstName("joe").lastName("biden")
				.phoneNumber("2222222222")
				.fileName("first_batch").build();
	}

	public static CustomerEntity createTestCustomerEntityC() {

		return CustomerEntity.builder().id(3L).firstName("donald").lastName("trump")
				.phoneNumber("3333333333")
				.fileName("second_batch").build();
	}

	public static CustomerEntity createTestCustomerEntityD() {

		return CustomerEntity.builder().id(4L).firstName("risi").lastName("sunak")
				.phoneNumber("4444444444")
				.fileName("second_batch").build();
	}

	public static CustomerEntity createTestCustomerEntityE() {

		return CustomerEntity.builder().id(5L).firstName("ranbir").lastName("kapoor")
				.phoneNumber("5555555555")
				.fileName("third_batch").build();
	}

	public static CustomerEntity createTestCustomerEntityF() {

		return CustomerEntity.builder().id(6L).firstName("vivek").lastName("ramasyamy")
				.phoneNumber("6666666666")
				.fileName("third_batch").build();
	}
}
