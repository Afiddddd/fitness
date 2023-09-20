package com.example.fitness.utils;

import com.example.fitness.dto.exception.ExceptionThrowable;
import com.example.fitness.model.user.UserModel;
import com.example.fitness.repository.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtService  {
    private static final int OTP_LENGTH = 6;
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;
    @Autowired
    private UserRepository userRepository;

    public JwtService() throws UnsupportedEncodingException {
    }

    public boolean isTokenExpired(String token) throws ExceptionThrowable {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws ExceptionThrowable {
        return extractClaim(token, Claims::getExpiration);
    }
    public String generateTokenLogin(String login,String userEmail) {
        return generateTokenLogin(userEmail);
    }

    private String  generateTokenLogin(String userEmail) {
        return Jwts.builder().setSubject(userEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 120 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }


    public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000L);
        System.err.println(expiryDate);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] decode = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(decode);
    }

    public String extractUserId(String token) throws ExceptionThrowable {
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) throws ExceptionThrowable {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) throws ExceptionThrowable {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
           e.printStackTrace();
            throw new ExceptionThrowable(HttpStatus.REQUEST_TIMEOUT.value(), "Link already expired");
        }
    }
    public boolean isTokenValid(String token, UserDetails userDetails) throws ExceptionThrowable {
        final String userName = extractUserId(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


}
