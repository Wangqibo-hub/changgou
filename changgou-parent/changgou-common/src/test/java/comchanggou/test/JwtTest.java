package comchanggou.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-19 20:06
 * @description
 */
public class JwtTest {

    @Test
    public void testCreatJwt(){
        JwtBuilder builder = Jwts.builder()
                .setId("888")
                .setSubject("小白")
                .setIssuedAt(new Date())
                //.setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS256, "itcast");

        //自定义数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 18);
        map.put("address", "深圳黑马");
        builder.addClaims(map);
        System.out.println(builder.compact());
    }

    @Test
    public void testParseJwt(){
        String value = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE1OTc4Mzk2MTcsImFkZHJlc3MiOiLmt7HlnLPpu5HpqawiLCJuYW1lIjoi5byg5LiJIiwiYWdlIjoxOH0.AwS8gRR0cpSK0_C_r2QpZGNSCU31eHn0VUkvRayQ0O0";
        Claims claims = Jwts.parser().setSigningKey("itcast").parseClaimsJws(value).getBody();
        System.out.println(claims);
    }
}
