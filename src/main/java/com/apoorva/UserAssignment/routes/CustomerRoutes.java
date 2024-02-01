package com.apoorva.UserAssignment.routes;

import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apoorva.UserAssignment.domain.CustomerEntity;
import com.apoorva.UserAssignment.service.CustomerService;

/*
 * Camel Routes for rest endpoints.
 * 1. getAllCustomers
 * 2. deleteById
 * 3. deleteByFileName 
 */
@Component
public class CustomerRoutes extends RouteBuilder {

	@Autowired
	private CustomerService customerService;

	@Override
	public void configure() throws Exception {
		restConfiguration().bindingMode(RestBindingMode.json);

		rest("/customers")
				.get("/getAll")
				.produces("application/json")
				.to("direct:getAll")
				.delete("/deleteById/{id}")
				.to("direct:deleteById")
				.delete("/deleteByFilename/{filename}")
				.to("direct:deleteByFilename");

		
		from("direct:getAll")
			.bean(customerService, "getAll");

		from("direct:deleteById")
				.process(exchange -> {
					// get id from pathParam
					String id = exchange.getIn().getHeader("id", String.class);
					log.info("Deleting Customer with id {}",id);
					Optional<CustomerEntity> customer = customerService.deleteById(Long.parseLong(id));
					if (customer.isPresent())
						exchange.getIn().setBody(customer.get());
					else {
						log.error("Failed to delete customer.");
						exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
						exchange.getIn().setBody("Customer with id " + id + " don't exist");
					}
				});

		from("direct:deleteByFilename")
				.process(exchange -> {
					// get filename from pathParam
					String filename = exchange.getIn().getHeader("filename", String.class);
					log.info("Deleting Customers of file {}",filename);
					int size = customerService.deleteByFilename(filename);
					log.info("{} items deleted.",size);
					if (size > 0)
						exchange.getIn().setBody(size + " items" + " deleted");
					else {
						exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
						exchange.getIn().setBody("No items with filename: " + filename + " exist");
					}
				});

	}

}
