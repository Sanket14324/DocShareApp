package docshare.com.service.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {



    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers( "/css/**", "/images/**", "/js/**").permitAll();
                    auth.requestMatchers("/register", "/login","/").permitAll();
                    auth.requestMatchers("/user/**").hasAuthority("USER");
                    auth.requestMatchers("/admin/**").hasAuthority("ADMIN");
                    auth.requestMatchers("/ticket/**", "/download/**").hasAnyAuthority("USER", "ADMIN");
                    auth.anyRequest().permitAll();})
                .formLogin(form -> {
                    form.loginPage("/login");
                    form.permitAll();
                })
                .logout(logout -> {
                    logout.invalidateHttpSession(true);
                    logout.clearAuthentication(true);
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
                    logout.logoutSuccessUrl("/login");
                    logout.permitAll();

                })
                .authenticationProvider(authenticationProvider())
                .build();    }






    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }



}

