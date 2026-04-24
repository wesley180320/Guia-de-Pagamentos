package com.gerador.pagamento;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeradorDePagamentoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GeradorDePagamentoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}

}
