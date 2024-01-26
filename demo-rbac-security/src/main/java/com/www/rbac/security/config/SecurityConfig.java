package com.www.rbac.security.config;

import com.www.rbac.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @Description SecurityConfig
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
@EnableConfigurationProperties(CustomConfig.class)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomConfig customConfig;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
   public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 关闭 CSRF
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)// 登录行为由自己实现，参考 AuthController#login
                .cors(Customizer.withDefaults())
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);// 登录行为由自己实现，参考 AuthController#login

        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                        .requestMatchers("@rbacAuthorityService.hasPermission(request,authentication)").permitAll()// RBAC 动态 url 认证
                        .anyRequest().authenticated()); // 所有请求都需要登录访问

        // 因为使用了JWT，所以这里不管理Session
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 放行所有不需要登录就可以访问的请求，参见 AuthController
     * 也可以在 {@link #(HttpSecurity)} 中配置
     * {@code http.authorizeRequests().antMatchers("/api/auth/**").permitAll()}
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            // 忽略 GET
            customConfig.getIgnores().getGet().forEach(url -> web.ignoring().requestMatchers(HttpMethod.GET, url));

            // 忽略 POST
            customConfig.getIgnores().getPost().forEach(url -> web.ignoring().requestMatchers(HttpMethod.POST, url));

            // 忽略 DELETE
            customConfig.getIgnores().getDelete().forEach(url -> web.ignoring().requestMatchers(HttpMethod.DELETE, url));

            // 忽略 PUT
            customConfig.getIgnores().getPut().forEach(url -> web.ignoring().requestMatchers(HttpMethod.PUT, url));

            // 忽略 HEAD
            customConfig.getIgnores().getHead().forEach(url -> web.ignoring().requestMatchers(HttpMethod.HEAD, url));

            // 忽略 PATCH
            customConfig.getIgnores().getPatch().forEach(url -> web.ignoring().requestMatchers(HttpMethod.PATCH, url));

            // 忽略 OPTIONS
            customConfig.getIgnores().getOptions().forEach(url -> web.ignoring().requestMatchers(HttpMethod.OPTIONS, url));

            // 忽略 TRACE
            customConfig.getIgnores().getTrace().forEach(url -> web.ignoring().requestMatchers(HttpMethod.TRACE, url));

            // 按照请求格式忽略
            customConfig.getIgnores().getPattern().forEach(url -> web.ignoring().requestMatchers(url));

        };
    }


}
