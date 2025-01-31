package com.servico.sistemadepagamento;

import com.servico.sistemadepagamento.dto.PixConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties(PixConfig.class)
@EnableFeignClients
@EnableCaching
public class SistemadepagamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemadepagamentoApplication.class, args);
	}

}
