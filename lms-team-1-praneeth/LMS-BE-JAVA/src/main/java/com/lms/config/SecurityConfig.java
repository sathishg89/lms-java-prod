package com.lms.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtFilterConfig jfl;

	@Bean
	SecurityFilterChain sfc(HttpSecurity http) throws Exception {

		String origins = "*";

		http.csrf(csrf -> csrf.disable());
		http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				List<String> listoforigin = List.of(origins);
				List<String> listofmethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH");
				List<String> listofheaders = List.of("*");
				List<String> listofexposedheaders = List.of("Authorization");

				CorsConfiguration cfg = new CorsConfiguration();
				cfg.setAllowedOrigins(listoforigin);
				cfg.setAllowedMethods(listofmethods);
				cfg.setAllowedHeaders(listofheaders);
				cfg.setExposedHeaders(listofexposedheaders);
				// cfg.setAllowCredentials(true);
				return cfg;
			}
		}));

		http.authorizeHttpRequests(

				auth ->

				{
					auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**",
							"/swagger-resources/**").permitAll();

					auth.requestMatchers("/admin/getallapi").permitAll();

					auth.requestMatchers("/auth/login").permitAll();

					auth.requestMatchers("/user/connect", "/user/uploadimage/{userEmail}",
							"/user/downloadimage/{userEmail}", "/user/update/{userEmail}", "/user/getotp/{userEmail}",
							"/user/verifyotp", "/user/resetpassword", "/user/{userEmail}/uploadresume",
							"/user/{userEmail}/getresume", "/user/{userEmail}/deleteresume",
							"/user/{courseName}/{trainerName}/getvideos").permitAll();

					auth.requestMatchers("/admin/signup", "/admin/importusers", "/admin/userupdate/{userEmail}",
							"/admin/userdelete/{userEmail}",
							"/admin/removecourseaccess/{userEmail}/{courseName}/{courseTrainer}").authenticated();

					auth.requestMatchers("/admin/course/addcourseuser", "/admin/course/addcourse",
							"/admin/course/updatecourse/{courseName}/{trainerName}", "/admin/course/accesscoursetouser",
							"/admin/course/savevideo", "/admin/course/getcourseuserinfo/{userEmail}",
							"/admin/course/getcourseusers/{courseName}/{trainerName}",

							"/admin/course/deletecourse/{courseName}/{trainerName}",
							"/admin/course/{courseName}/{trainerName}/getvideos",

							"/admin/course/getallcourses", "/admin/course/{courseName}/courseinfo",

							"/admin/course/{courseName}/{trainerName}/getmodules",

							"/admin/course/{courseName}/{moduleId}/updatemodules",

							"/admin/course/{courseName}/{moduleId}/deletemodule").permitAll();

				}).sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(ap()).addFilterBefore((Filter) jfl, UsernamePasswordAuthenticationFilter.class);

		http.formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	UserDetailsService uds() {
		return new UserUserdetailsService();
	}

	@Bean
	AuthenticationProvider ap() {
		DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
		dap.setUserDetailsService(uds());
		dap.setPasswordEncoder(pe());
		return dap;
	}

	@Bean
	PasswordEncoder pe() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager am(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
