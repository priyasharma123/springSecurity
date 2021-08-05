package com.spring.security.BasicAuth;

import com.spring.security.auth.ApplicationUserService;
import com.spring.security.jwt.JWTConfig;
import com.spring.security.jwt.JWTTokenVerifier;
import com.spring.security.jwt.JWTUsernameAndPassAndFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

import static com.spring.security.enums.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BasicAuthApplication extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    private final JWTConfig jwtConfig;

    private final SecretKey secretKey;

    @Autowired
    public BasicAuthApplication(PasswordEncoder passwordEncoder, JWTConfig jwtConfig, SecretKey secretKey, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.applicationUserService = applicationUserService;
    }

    private final ApplicationUserService applicationUserService;

    /* This method will display login popup*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//without disabling this you will be unable to perform put/delete/post operations
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTUsernameAndPassAndFilter(authenticationManager(), jwtConfig, secretKey)) // adding filter for JWT based authentication
                .addFilterAfter(new JWTTokenVerifier(jwtConfig, secretKey),JWTUsernameAndPassAndFilter.class)
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*")
                .permitAll()
                .antMatchers("/api/**")
                .hasRole(STUDENT.name())
//                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission()) //user who have this permission able to perform this ops
//                .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(),ADMINTRANIEE.name())
                .anyRequest()
                .authenticated();
//                .and()
//                .formLogin()
//                .loginPage("/login").permitAll()
//                        .defaultSuccessUrl("/courses", true)
//                        .passwordParameter("password")
//                        .usernameParameter("username")
//                .and()
//                .rememberMe()//default jsession id stay active for 30 mins using remember me it will stay for 2 weeks
//                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//                .key("somethingverysecured")
//                .rememberMeParameter("remember-me")
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID", "remember-me")
//                .logoutSuccessUrl("/login");
        //.httpBasic();
    }

    @Bean
    @Autowired
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);//allows pass to decoded
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()); //custom auth provider
    }
    //In memory Db
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails priyaUser = User.builder() //here we are building a user and password
//                                .username("priya")
//                                .password(passwordEncoder.encode("pass"))/*if we don't add
//                                                            passwordEncoder we will get error . It will encode the pass*/
//                                //.roles(STUDENT.name()) //ROLE_STUDENT it will generate that as role
//                                .authorities(STUDENT.getGrantedAuthorities())
//                                .build();
//
//        UserDetails nikhilUser = User.builder() //here we are building a user and password
//                .username("nikhil")
//                .password(passwordEncoder.encode("pass"))/*if we don't add
//                                                            passwordEncoder we will get error . It will encode the pass*/
//                //.roles(ADMIN.name()) //ROLE_STUDENT it will generate that as role
//                .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails shubhamUser = User.builder() //here we are building a user and password
//                .username("shubham")
//                .password(passwordEncoder.encode("pass"))/*if we don't add
//                                                            passwordEncoder we will get error . It will encode the pass*/
//                //.roles(ADMINTRANIEE.name()) //ROLE_STUDENT it will generate that as role
//                .authorities(ADMINTRANIEE.getGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(priyaUser,nikhilUser,shubhamUser); //here we are using in memory
//    }

}
