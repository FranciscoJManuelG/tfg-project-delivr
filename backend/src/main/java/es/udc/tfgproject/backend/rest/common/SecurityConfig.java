package es.udc.tfgproject.backend.rest.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtGenerator jwtGenerator;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().addFilter(new JwtFilter(authenticationManager(), jwtGenerator)).authorizeRequests()
				.antMatchers("/users/signUp").permitAll().antMatchers("/users/login").permitAll()
				.antMatchers("/users/loginFromServiceToken").permitAll().antMatchers("/users/signUpBusinessman")
				.permitAll().antMatchers("/business/companies/categories").permitAll().antMatchers("/business/cities").permitAll().antMatchers("/reservation/*").permitAll()
				.antMatchers("/businessCatalog/companies").permitAll().antMatchers("/productCatalog/products/{companyId}/categories").permitAll()
				.antMatchers("/productCatalog/*").permitAll().antMatchers("/shopping/shoppingCarts/*").permitAll().antMatchers("/reservation/menus/checkCapacity").permitAll()
				.antMatchers("/management/products/categories").permitAll().antMatchers("/management/products/{companyId}").permitAll()
				.anyRequest().hasAnyRole("CLIENT", "BUSINESSMAN", "ADMIN");

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration config = new CorsConfiguration();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		source.registerCorsConfiguration("/**", config);

		return source;

	}

}
