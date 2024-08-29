package ra.project_module04.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import ra.project_module04.exception.CustomException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exc) throws IOException, ServletException {
        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.warn("User: " + auth.getName()
                    + " đã cố gắng truy cập vào URL được bảo vệ: ");
        }

        response.setHeader("error","authorize");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(403);
        Map<String , Object> map =new HashMap<>();
        map.put("error",new CustomException(exc.getMessage(),HttpStatus.FORBIDDEN));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(),map);
    }
}
