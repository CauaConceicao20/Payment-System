package com.servico.sistemadepagamento;

import com.servico.sistemadepagamento.dto.PixConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PixConfig.class)
public class SistemadepagamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemadepagamentoApplication.class, args);
	}

}
