package edu.tcu.cs.hogwartsartifactsonline.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.AntPathMatcher;

import java.beans.Customizer;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static jdk.internal.classfile.Classfile.build;
import static jdk.internal.net.http.common.Log.headers;

@Configuration
public class SecurityConfiguration
{
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    @Value("{api.endpoint.base-url}")
    private String baseUrl;

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
    public SecurityConfiguration(CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler, CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint, CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint) throws NoSuchAlgorithmException {
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customBearerTokenAuthenticationEntryPoint = customBearerTokenAuthenticationEntryPoint;
        //generate a public/private key pair.
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair= keyPairGenerator.generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    return http
            .authorizeHttpRequest(authorizeHttpRequests -> authorizeHttpRequests


                    .requestMatchers(HttpMethod.GET, this.baseUrl + "/artifacts/**").permitAll()
            .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/**").hasAuthority("ROLE_admin"))//protecting the endpoint
            .requestMatchers(HttpMethod.POST, this.baseUrl + "/users").hasAuthority("ROLE_admin")//protecting the endpoint
            .requestMatchers(HttpMethod.PUT, this.baseUrl + "/users/**").hasAuthority("ROLE_admin")//protecting the endpoint
            .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/users/**").hasAuthority("ROLE_admin")//protecting the endpoint
    .requestMatchers(AntPathrequestMatcher.antMatcher("/h2-console/**")).permitAll()
            .anyRequest().authenitcated()
)
    .headers(headers -> headers.frameOptions().disable())
            .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
            .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.customBasicAuthenticationEntryPoint))
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt())
                .and()
                        .accessDeniedHandler(this.customBearerTokenAccessDeniedHandler)
                .authenticationEntryPoint(this.customBearerTokenAuthenticationEntryPoint)
                .sessionManagement(sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();


    }
@Bean
    public PasswordEncoder passwordEncoder(){
    retrun new BCryptPassowrdEncoder(12);

    }
    @Bean
public JwtEncoder jwtEncoder(){
JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
    JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSet);

}
@Bean
public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();

}
@Bean
public JwtAuthenticationConverter jwtAuthenticationConverter(){
     JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

     jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
     jwtGratedAuthoritiesConverter.setAuthorityPrefix("");

     JwtAuthenticationConverter jwtAuthenticationConverter1 = new JwtAuthenticationConverter();
     jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGratedAuthoritiesConverter);
     return jwtAuthenticationConverter;

        return null;
}

}