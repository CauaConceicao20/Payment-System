package com.servico.sistemadepagamento.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${jwt.public-key-path}")
    private String publicKeyPath;

    private JWTVerifier jwtVerifier;
    private RSAPublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            loadKey(publicKeyPath);
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            this.jwtVerifier = JWT.require(algorithm).build();
        } catch (Exception e) {
            throw new RuntimeException("Error loading private key", e);
        }
    }

    public void loadKey(String publicKeyPath) throws Exception {
        publicKey = loadPublicKey(publicKeyPath);
    }

    public RSAPublicKey loadPublicKey(String publicKeyPath) throws Exception {
        FileInputStream publicKeyInputStream = new FileInputStream(new File(publicKeyPath));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyInputStream.readAllBytes()));
        return publicKey;
    }

    public List<String> getRoles(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256((java.security.interfaces.RSAPublicKey) publicKey, null);

            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("roles").asList(String.class);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter roles do token: " + e.getMessage(), e);
        }
    }

    public List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256((java.security.interfaces.RSAPublicKey) publicKey, null);
            return JWT.require(algorithm)
                    .withIssuer("E-commerce")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token inválido ou expirado", exception);
        }
    }

    public boolean validateToken(String token) {
        try {
            jwtVerifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.err.println("Erro ao validar o token: " + e.getMessage());
            return false;
        }
    }
}

