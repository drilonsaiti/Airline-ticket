package airlanetickets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final CustomUsernamePasswordAuthenticationProvider authenticationProvider;

    public WebSecurityConfig(PasswordEncoder passwordEncoder,
                             CustomUsernamePasswordAuthenticationProvider authenticationProvider) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //Web resources
        web.ignoring().antMatchers("/resources/**");
        web.ignoring().antMatchers("resources/**");

        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/scripts/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/fonts/**");
        web.ignoring().antMatchers("/scss/**");
        web.ignoring().antMatchers("/doc/**");

        web.ignoring().antMatchers("/img/**");
        web.ignoring().antMatchers("img/**");
        web.ignoring().antMatchers("css/**");
        web.ignoring().antMatchers("js/**");
        web.ignoring().antMatchers("js/vendor/**");
        web.ignoring().antMatchers("/js/vendor/**");
        web.ignoring().antMatchers("/doc/**");

        web.ignoring().antMatchers("fonts/**");
        web.ignoring().antMatchers("scss/**");
        web.ignoring().antMatchers("scss/bootstrap/**");
        web.ignoring().antMatchers("scss/bootstrap/mixins/**");
        web.ignoring().antMatchers("scss/bootstrap/utilities/**");
        web.ignoring().antMatchers("scss/theme/**");

        web.ignoring().antMatchers("doc/**");
        web.ignoring().antMatchers("doc/css/**");
        web.ignoring().antMatchers("/doc/css/**");
        web.ignoring().antMatchers("doc/fonts/**");
        web.ignoring().antMatchers("doc/img/**");
        web.ignoring().antMatchers("/doc/img/**");
        web.ignoring().antMatchers("doc/js/**");
        web.ignoring().antMatchers("doc/syntax-highlighter/**");

        web.ignoring().antMatchers("scripts/**");
        web.ignoring().antMatchers("/scripts/**");

    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/resources/**", "/static/**", "/css/**","/doc/**","/resources/**",
                        "/js/**", "/img/**","/vendor/**","img/**","/fonts/**","/scss/**","/Travel - Doc/**", "/assets/**","/api/**","pdflogo.png","/doc/**","doc/**").permitAll()
                .antMatchers("/flights/**","/home","/","/register","**/resources/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/*.js").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .failureUrl("/login?error=BadCredentials")
                .defaultSuccessUrl("/home", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }


}
