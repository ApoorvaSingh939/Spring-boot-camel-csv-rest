package com.apoorva.UserAssignment.routes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apoorva.UserAssignment.domain.CustomerEntity;
import com.apoorva.UserAssignment.service.CustomerService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomerCsvRoutes extends RouteBuilder {

	@Autowired
	private CustomerService customerService;

	@Override
	public void configure() throws Exception {
		DataFormat bindy = new BindyCsvDataFormat(CustomerEntity.class);
		SharedState state = new SharedState();
		from("file:data/input?noop=true").routeId("csv-input")
				.log("detected a file")
				.process(e -> state.reset())
				.doTry()
				.unmarshal(bindy)
				.to("direct:csvFileProcessor")
				.endDoTry()
				.doCatch(IllegalArgumentException.class)
				.log("Failed to parse csv file, invalid csv file given")
				.process(e -> state.setErrorFlag(true))
				.to("direct:moveFile");

		from("direct:csvFileProcessor")
				.split(body())
				.process(exchange -> {
					CustomerEntity customer = exchange.getIn().getBody(CustomerEntity.class);
					String filename = exchange.getIn().getHeader("CamelFileName", String.class);
					customer.setFileName(filename);
					try {
						customerService.addCustomer(customer);
						log.info("Successfully added new customer {}",customer.toString());
						state.incrementSuccssFul();

					} catch (Exception e) {
						log.error("failed to add new customer {}",customer.toString());
						state.incrementUnSuccssFul();
						state.setErrorFlag(true);
					}
				})
				.end()
				.to("direct:moveFile");

		from("direct:moveFile")
				.process(e -> {
					String filename = e.getIn().getHeader("CamelFileName", String.class);
					Path sourcePath = Paths
							.get("data/input/" + filename);
					Path destinationPath = Paths
							.get("data/output/" + (state.isErrorFlag() ? ".failed/" : ".done/") + filename);

					Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
					log.info("file moved to {} ", destinationPath.toString());
					log.info("Total successfully added customers: {}", state.getSuccessFul());
					log.info("Total failed to add customers: {}" , state.getUnSuccessFul());
				}
				);
		
	}

}

/*
 * A class to maintain success and failed count in a csv file.
 */
@Getter
@Setter
class SharedState {
	private boolean errorFlag;
	private boolean csvParsingErrorFlag;
	private int successFul = 0;
	private int unSuccessFul = 0;

	public void reset() {
		setErrorFlag(false);
		setSuccessFul(0);
		setUnSuccessFul(0);
		setCsvParsingErrorFlag(false);
	}

	public void incrementSuccssFul() {
		setSuccessFul(getSuccessFul() + 1);
	}

	public void incrementUnSuccssFul() {
		setUnSuccessFul(getUnSuccessFul() + 1);
	}

}
