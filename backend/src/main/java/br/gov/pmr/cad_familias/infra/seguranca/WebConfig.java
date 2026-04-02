package br.gov.pmr.cad_familias.infra.seguranca;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

	@Value("${cors.allowed-origins}")
	private String allowedOrigins;

	@Value("${cors.allow-headers}")
	private String allowedHeaders;

	@Value("${cors.allow-methods}")
	private String allowedMethods;
	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOriginPatterns(allowedOrigins)  // suporta múltiplas origens
						.allowedMethods(allowedMethods.split(",\\s*"))
						.allowedHeaders(allowedHeaders.split(",\\s*"))
						.allowCredentials(true);
			}
		};
	}
}
