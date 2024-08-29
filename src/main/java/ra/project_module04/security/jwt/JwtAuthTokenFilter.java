package ra.project_module04.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;
import ra.project_module04.security.principle.MyUserDetailServiceCustom;

import java.io.IOException;

//Bộ lọc các chuỗi token ra
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private MyUserDetailServiceCustom detailServiceCustom;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Xử lý token và giải mã -> Cấp quyền và lưu nó vào SecurityContext
        // lấy tokken
        String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)){
            String username = jwtProvider.getUserFromToken(token);
            UserDetails userDetails = detailServiceCustom.loadUserByUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            // lưu authentication vào security context
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request,response);
    }

    //Lấy ra chuỗi bearer token
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer!=null && bearer.startsWith("Bearer ")){
            return bearer.substring("Bearer ".length());
        }
        return null;
    }
}
