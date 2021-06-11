package com.shareadda.api.ShareAdda.Utils;

import com.shareadda.api.ShareAdda.Constants.SecurityConstants;
import com.shareadda.api.ShareAdda.User.Service.impl.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTUtils {

    //Generate the token
    public String generateToken(Authentication authentication){
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime()+ SecurityConstants.EXPIRATION_TIME);
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",user.getUsername());


        return Jwts.builder()
                .setSubject(user.getUsername())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.SECRET)
                .compact();

    }
    //validate the token
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            System.out.println("Invalid JWT SIgnature");
        }catch (MalformedJwtException ex){
            System.out.println("Invalid Jwt Token");
        }catch (ExpiredJwtException ex){
            System.out.println("Expired Jwt Token");
        }catch (UnsupportedJwtException ex){
            System.out.println("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }
    //Get user Id fro token
    public String getUserUsernameFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
        return (String)claims.get("username");
    }
}
