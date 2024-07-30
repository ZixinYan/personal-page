package com.zixin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    /*
    @Test
    public void testGen(){
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","admin");
        //generate
        String token = JWT.create()
                .withClaim("user",claims)//add information
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60))//life
                .sign(Algorithm.HMAC256("GenShinImpactIsTheBestGameInWorld"));
        System.out.println(token);
    }
    @Test
    public void testParse(){
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6ImFkbWluIn0sImV4cCI6MTcyMDI4NTg3M30.V_-SYgUZDLKRa3ulN_GZ7To-bNSGITmHGoko2U0JYNg";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("GenShinImpactIsTheBestGameInWorld")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));

    }

     */
}
