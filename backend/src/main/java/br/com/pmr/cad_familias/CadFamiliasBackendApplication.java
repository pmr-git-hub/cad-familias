package br.com.pmr.cad_familias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.pmr.cad_familias")
public class CadFamiliasBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadFamiliasBackendApplication.class, args);
	}

}
