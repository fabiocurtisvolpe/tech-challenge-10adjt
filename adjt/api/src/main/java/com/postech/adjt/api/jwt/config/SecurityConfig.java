package com.postech.adjt.api.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.postech.adjt.api.exception.CustomAccessDeniedHandler;
import com.postech.adjt.api.exception.CustomAuthenticationEntryPoint;
import com.postech.adjt.api.jwt.filter.JwtAuthenticationFilter;
import com.postech.adjt.api.jwt.service.AppUserDetailsService;

/**
 * Classe de configuração de segurança da aplicação.
 * 
 * <p>
 * Define a política de autenticação e autorização utilizando JWT,
 * desabilita CSRF, configura sessões como stateless e registra o filtro
 * de autenticação JWT.
 * </p>
 * 
 * <p>
 * Utiliza {@link AppUserDetailsService} para carregar os dados do usuário
 * e {@link JwtAuthenticationFilter} para interceptar requisições protegidas.
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-08
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        /**
         * Serviço responsável por carregar os dados do usuário para autenticação.
         */
        private final AppUserDetailsService appUserDetailsService;

        private final CustomAccessDeniedHandler accessDeniedHandler;

        private final CustomAuthenticationEntryPoint authenticationEntryPoint;

        /**
         * Construtor com injeção do serviço de usuários.
         *
         * @param appUserDetailsService Serviço de autenticação de usuários.
         */
        public SecurityConfig(AppUserDetailsService appUserDetailsService,
                        CustomAccessDeniedHandler accessDeniedHandler,
                        CustomAuthenticationEntryPoint authenticationEntryPoint) {
                this.appUserDetailsService = appUserDetailsService;
                this.accessDeniedHandler = accessDeniedHandler;
                this.authenticationEntryPoint = authenticationEntryPoint;
        }

        /**
         * Configura a cadeia de filtros de segurança da aplicação.
         *
         * <p>
         * Define as URLs públicas, exige autenticação para demais rotas,
         * desabilita CSRF e aplica política de sessão stateless.
         * </p>
         *
         * <p>
         * Adiciona o filtro {@link JwtAuthenticationFilter} antes do filtro padrão
         * {@link UsernamePasswordAuthenticationFilter}.
         * </p>
         *
         * @param http      Configuração de segurança HTTP.
         * @param jwtFilter Filtro de autenticação JWT.
         * @return Cadeia de filtros de segurança.
         * @throws Exception Em caso de erro na configuração.
         */
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter)
                        throws Exception {
                http.csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/swagger-ui/**").permitAll()
                                                .requestMatchers("/swagger-ui.html").permitAll()
                                                .requestMatchers("/v3/api-docs/**").permitAll()
                                                .requestMatchers("/swagger-resources/**").permitAll()
                                                .requestMatchers("/webjars/**").permitAll()
                                                .requestMatchers("/api/login/**", "/api/usuario/criar").permitAll()
                                                .requestMatchers("/api/tipo-usuario/**").permitAll()
                                                .anyRequest().authenticated())
                                .exceptionHandling(ex -> ex
                                                .accessDeniedHandler(accessDeniedHandler)
                                                .authenticationEntryPoint(authenticationEntryPoint))
                                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        /**
         * Configura o gerenciador de autenticação da aplicação.
         *
         * <p>
         * Utiliza o {@link AppUserDetailsService} e o {@link PasswordEncoder}
         * para validar credenciais dos usuários.
         * </p>
         *
         * @param http Configuração de segurança HTTP.
         * @return Gerenciador de autenticação.
         * @throws Exception Em caso de erro na configuração.
         */
        @Bean
        AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder
                                .userDetailsService(appUserDetailsService)
                                .passwordEncoder(passwordEncoder());
                return authenticationManagerBuilder.build();
        }

        /**
         * Bean responsável por codificar senhas usando o algoritmo BCrypt.
         *
         * @return Instância de {@link BCryptPasswordEncoder}.
         */
        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}