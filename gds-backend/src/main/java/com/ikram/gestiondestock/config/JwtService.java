package com.ikram.gestiondestock.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "6879f9cdfb120bb3bf9610f0047ab4c89d04b37d5ff540ebd0d86f73d3a6c188";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Claims extractAllClaims(String token){
        //le parserBuilder est un constructeur d'un parser
        //le parser sert à décoder un token existant et à vérifier sa signature
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) //clé pr vérifier la signature
                .build() //construit le parser
                .parseClaimsJws(token) //token à décoder + vérification
                .getBody(); //payload (claims)
        // parserBuilder() = analyse et valide un JWT.
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder() //crée le builder pr construire le jwt
                .setClaims(extraClaims) // insère dans le payload toutes les claims passées via la map
                .setSubject(userDetails.getUsername()) //claim sub
                .setIssuedAt(new Date(System.currentTimeMillis())) //claim iat
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //claim exp (expire dans 24h)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) //signature avec clé secrète
                .compact(); //génère la chaîne finale HEADER.PAYLOAD.SIGNATURE
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
