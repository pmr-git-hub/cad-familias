package br.gov.pmr.cad_familias.infra.seguranca;

import br.gov.pmr.cad_familias.infra.seguranca.usuario.UsuarioLogadoResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final UsuarioLogadoResolver usuarioLogadoResolver;

	public WebConfig(UsuarioLogadoResolver usuarioLogadoResolver) {
		this.usuarioLogadoResolver = usuarioLogadoResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(usuarioLogadoResolver);
	}
}
