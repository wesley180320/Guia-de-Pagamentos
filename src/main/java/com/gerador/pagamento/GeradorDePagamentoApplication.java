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
//		String pastaProjeto = "pdfs"; // pasta dentro do projeto
//		File dir = new File(pastaProjeto);
//		if (!dir.exists()) {
//			dir.mkdirs(); // cria a pasta se não existir
//		}
//		String caminho = pastaProjeto + "/guia.pdf";
//
//		GerarGuia.gerarGuiaPdf(
//				caminho,
//				"Wesley",
//				"123.456.789-00",
//				"Rua Teste, 123",
//				new BigDecimal("150.00"),
//				LocalDate.now().plusDays(5),
//				"chave-pix-exemplo",
//				"Recebedor Teste",
//				"São Paulo"
//		);
//		System.out.println("PDF gerado com sucesso!");
	}

}
