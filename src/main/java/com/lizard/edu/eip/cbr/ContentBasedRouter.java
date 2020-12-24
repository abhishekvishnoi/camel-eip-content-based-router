package com.lizard.edu.eip.cbr;

import java.io.BufferedReader;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ContentBasedRouter extends RouteBuilder{
	
	private static final String FILE_TYPE = "fileType";
	
	Processor wareHouseTypeProcessor = new Processor() {
		
		@Override
		public void process(Exchange exchange) throws Exception {
			
			BufferedReader body = exchange.getIn().getBody(BufferedReader.class);
			
			String firstLine = body.readLine();
			
			exchange.getIn().setHeader(FILE_TYPE, firstLine);
			
		}
	};

	/**
	 * I003 - Content Based Routing
	 */
	@Override
	public void configure() throws Exception {

		from("{{input.file}}")
			.log(LoggingLevel.DEBUG, log, "New message received")
			.process(wareHouseTypeProcessor)
			.choice()
			.when(header(FILE_TYPE).isEqualTo("W174"))
			.to("{{output.file.w174}}")
			.when(header(FILE_TYPE).isEqualTo("W180"))
			.to("{{output.file.w180}}")
			.otherwise()
			.log("The type of the warehouse is incorrect ${header.fileType}");

		
	}

}
