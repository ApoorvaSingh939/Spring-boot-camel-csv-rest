package com.apoorva.UserAssignment.routes;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Configuration;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.apoorva.UserAssignment.CustomersUtils;
import com.apoorva.UserAssignment.domain.CustomerEntity;
import com.apoorva.UserAssignment.exceptions.BadRequestException;
import com.apoorva.UserAssignment.service.CustomerService;

@CamelSpringBootTest
@SpringBootTest
@EnableAutoConfiguration
@MockEndpoints("direct:*")
@ExtendWith(MockitoExtension.class)
public class CustomerCsvRouteTest {

	private ProducerTemplate producerTemplate;

	@Autowired
	private CamelContext context;

	@MockBean
	private CustomerService customerService;

	@EndpointInject("mock:direct:csvFileProcessor")
	private MockEndpoint mockCsvFileProcessor;

	@EndpointInject("mock:direct:moveFile")
	private MockEndpoint mockMoveFiles;

	@Configuration
	static class TestConfig {

		RoutesBuilder route() {
			return new CustomerCsvRoutes();
		}
	}

	@BeforeEach
	public void setup() {
		producerTemplate = context.createProducerTemplate();
		mockMoveFiles.reset();
		mockCsvFileProcessor.reset();
	}

	// @Test
	// void testWetherFileComponentMoveFileToFileProcessor() throws Exception {
	// 	File file = new File("data/test/test.csv");
	// 	mockCsvFileProcessor.expectedMessageCount(1);
	// 	producerTemplate.sendBodyAndHeader("file:data/input?noop=true", file, Exchange.FILE_NAME, file.getName());
	// 	mockCsvFileProcessor.assertIsSatisfied();
	// }

	@Test
	void testWeatherANonCsvCallMoveFile() throws Exception {
		File file = new File("data/test/testTwo.csv");
		mockMoveFiles.expectedMessageCount(1);
		producerTemplate.sendBodyAndHeader("file:data/input?noop=true", file, Exchange.FILE_NAME, file.getName());
		mockMoveFiles.assertIsSatisfied();
	}

	@Test
	void testCsvFileProcessToCallsAddCustomerToThrowException() throws Exception {
		doThrow(new BadRequestException("PhoneNumber already exists")).when(customerService)
				.addCustomer(any(CustomerEntity.class));
		producerTemplate.send("direct:csvFileProcessor", exchange -> {
			exchange.getIn().setBody(CustomersUtils.createTestCustomerEntityA());
			exchange.getIn().setHeader("CamelFileName", "test.csv");
		});
		verify(customerService).addCustomer(any(CustomerEntity.class));
	}


	void testingFileRoute() throws Exception {

		doNothing().when(customerService).addCustomer(any());

		producerTemplate.send("file:data/input", exchange -> {
			exchange.getIn().setBody(new File("data/input/test.csv"));
			exchange.getIn().setHeader("CamelFileName", "test.csv");
		});

		Thread.sleep(1000L);
		assertFileMovedTo("data/output/.done/test.csv");
	}

	private void assertFileMovedTo(String expectedPath) {
		File expectedFile = new File(expectedPath);
		assertTrue(expectedFile.exists(), "File not found at expected path: " + expectedPath);
	}
}
