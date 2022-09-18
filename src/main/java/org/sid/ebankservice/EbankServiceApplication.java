package org.sid.ebankservice;
import org.sid.ebankservice.service.EBankServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class EbankServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(EBankServiceImpl eBankService){
		return args -> {
			eBankService.loadCurrencies();
		};
	}
}
