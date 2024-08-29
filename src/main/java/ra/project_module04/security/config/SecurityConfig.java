package ra.project_module04.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ra.project_module04.security.jwt.JwtAuthTokenFilter;
import ra.project_module04.security.principle.MyUserDetailServiceCustom;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthTokenFilter authTokenFilter;

    @Autowired
    private SecurityAuthenticationEntryPoint entryPoint;

    @Autowired
    private MyUserDetailServiceCustom detailServiceCustom;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(detailServiceCustom);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomDeniedHandler();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//Không cho phép lữu trữ phiên làm việc
                .authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/admin/**").hasRole("ADMIN"))
                                auth.requestMatchers("/api.example.com/v1/admin/**").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers("/api.example.com/v1/user/**").hasAuthority("ROLE_USER")
                                        .requestMatchers("/api.example.com/v1/manager/**").hasAuthority("ROLE_MANAGER")
                                        .requestMatchers("/api.example.com/v1/user-manager/**", "/api.example.com/v1/user-client/**").hasAnyAuthority("ROLE_USER", "ROLE_MANAGER")
                                        .anyRequest().permitAll() // tất cả quyền
                )
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(e -> e.authenticationEntryPoint(entryPoint).accessDeniedHandler(accessDeniedHandler()))
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



}
