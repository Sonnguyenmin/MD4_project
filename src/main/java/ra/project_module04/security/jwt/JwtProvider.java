package ra.project_module04.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    @Value("${jwt_expiration}")
    private Long expiration;

    @Value("${jwt_secret_key}")
    private String secretKey;


    //XÁc thực token
    public String generateAccessToken(UserDetails userDetails) {
        Date today = new Date();
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(today)
                .setExpiration(new Date(today.getTime() + expiration)) // 24 h
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();
    }

    //Kiểm tra xác thực
    public Boolean validateToken(String token) {
        try {
            //
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) { //Jwt hết hạn
            log.error("JWT: Thời gian đăng nhập hết hạn:", e.getMessage());
        } catch (UnsupportedJwtException e) {//Không hỗ trợ mã hóa
            log.error("JWT: message error unsupported:", e.getMessage());
        } catch (MalformedJwtException e) {//Không hợp lệ khi chuỗi linh tinh
            log.error("JWT: Chuỗi mã hóa khng đúng:", e.getMessage());
        } catch (SignatureException e) {//Chữ kí
            log.error("JWT: message error signature not math:", e.getMessage());
        } catch (IllegalArgumentException e) {//Đối số chuyền vào không hợp lệ
            log.error("JWT: message claims empty or argument invalid: ", e.getMessage());
        }
        return false;
    }

    //Giải mã token
    public String getUserFromToken(String token) {
        return  Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

}
