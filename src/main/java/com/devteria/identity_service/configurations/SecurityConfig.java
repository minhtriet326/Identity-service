package com.devteria.identity_service.configurations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity()
// public class SecurityConfig {
//    private final String[] PUBLIC_ENDPOINTS = {"/api/v1/user/**", "/api/v1/auth/**"};
//
//    @Value("${jwt.signerKey}")
//    String SIGNER_KEY;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeHttpRequests(request ->
//            request
//                    .requestMatchers("/v3/api-docs/**", "/swagger-ui.html",
// "/swagger-ui/**").permitAll()
//                    .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
//                    .requestMatchers(HttpMethod.GET,
// "api/v1/user/getAllUsers").hasAuthority("ROLE_ADMIN")
//                    .anyRequest().authenticated()
//        );
//
//        httpSecurity.oauth2ResourceServer(oauth2 ->
//                oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())
//                        .jwtAuthenticationConverter(customJwtAuthenticationConverter()))
//                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));// Lấy token
// từ header và gọi
// jwtDecoder
//
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
//        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }
//
//    private JwtAuthenticationConverter customJwtAuthenticationConverter() {
//        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
//
//        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new
// JwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//
//        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//
//        return authenticationConverter;
//    }
//
// }

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SecurityConfig {
    @NonFinal
    CustomJwtDecoder customJwtDecoder;

    private final String[] PUBLIC_REQUESTS = {"/api/v1/user/**", "/api/v1/auth/**"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(
                request ->
                        request
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, PUBLIC_REQUESTS)
                                .permitAll()
                                .anyRequest()
                                .authenticated());

        httpSecurity.oauth2ResourceServer(
                oauth ->
                        oauth
                                .jwt(
                                        jwtConfigurer ->
                                                jwtConfigurer
                                                        .decoder(customJwtDecoder)
                                                        .jwtAuthenticationConverter(customJwtAuthenticationConverter()))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        httpSecurity
                //            .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // tham số đầu tiên thể hiện chúng ta muốn áp dụng cho all các endpoints của chúng ta, tham số 2
        // là
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }

    @Bean
    public JwtAuthenticationConverter
    customJwtAuthenticationConverter() { // bỏ prefix SCOPE_ mặc định và thay bằng ""
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return converter;
    }
}
