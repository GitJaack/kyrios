package fr.cmp.kyrios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/login", "/css/**", "/images/**", "/h2-console/**")
                                                .permitAll()
                                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/applications/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/emplois/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/profil-app/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/profils-si/**").permitAll()
                                                .requestMatchers("/api/emplois/**").authenticated()
                                                .requestMatchers("/api/profils-si/**").authenticated()
                                                .requestMatchers("/api/categories-si/**").authenticated()
                                                .requestMatchers("/api/ressources-si/**").authenticated()
                                                .requestMatchers("/api/applications/**").authenticated()
                                                .requestMatchers("/api/profil-app/**").authenticated()
                                                .requestMatchers("/api/ressources-app/**").authenticated()
                                                .anyRequest().permitAll())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .exceptionHandling(ex -> ex
                                                .defaultAuthenticationEntryPointFor(
                                                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                                                request -> request.getServletPath()
                                                                                .startsWith("/api/")));

                return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                UserDetails user = User.builder()
                                .username("admin")
                                .password(passwordEncoder().encode("admin123"))
                                .roles("USER")
                                .build();

                return new InMemoryUserDetailsManager(user);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}