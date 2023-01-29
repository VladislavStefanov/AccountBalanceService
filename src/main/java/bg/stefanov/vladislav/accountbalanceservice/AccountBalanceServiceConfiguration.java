package bg.stefanov.vladislav.accountbalanceservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class AccountBalanceServiceConfiguration {
	
	@Bean
	public ObjectMapper getObjectMapper() {
		return JsonMapper.builder()
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build()
				.setSerializationInclusion(Include.NON_NULL)
				.registerModule(new JavaTimeModule());
		
	}
}
