package com.gerador.pagamento;

import com.gerador.pagamento.service.GerarGuia;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class GeradorDePagamentoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GeradorDePagamentoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}

}
