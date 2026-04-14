package br.gov.pmr.cad_familias.infra.seguranca;

import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogadoResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${cors.allowed-origins}")
	private String allowedOrigins;

	@Value("${cors.allow-headers}")
	private String allowedHeaders;

	@Value("${cors.allow-methods}")
	private String allowedMethods;

	private final UsuarioLogadoResolver usuarioLogadoResolver;

	public WebConfig(UsuarioLogadoResolver usuarioLogadoResolver) {
		this.usuarioLogadoResolver = usuarioLogadoResolver;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns(allowedOrigins)
				.allowedMethods(allowedMethods.split(",\\s*"))
				.allowedHeaders(allowedHeaders.split(",\\s*"))
				.allowCredentials(true);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(usuarioLogadoResolver);
	}
}
