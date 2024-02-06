package com.example.authapi.middlewares;

import com.example.authapi.utils.HandleErrorFields;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/*
 * Вы то, что делаете постоянно. Если вы стремитесь к совершенству, то это не случайность. Это привычка.
 */

// валидация токена
@Component
public class ValidateToken extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    final String BEARER_PREFIX = "Bearer ";

    try {
      // вытаскивает токен из заголовка
      String tokenHeader = request.getHeader("Authorization");

      // собирает переменные env файла
      Dotenv dotenv = Dotenv.configure().load();

      if (getValidateIgnoredPath(request)) {
        filterChain.doFilter(request, response);
        return;
      }

      if (tokenHeader == null || !tokenHeader.startsWith(BEARER_PREFIX)) {
        throw new JwtException("");
      }

      // срезает Bearer и вытаскивает токен
      String token = tokenHeader.substring(BEARER_PREFIX.length());

      // проверяет токен на валидность
      String payload = Jwts.parser()
              .verifyWith(Keys.hmacShaKeyFor(dotenv.get("SECRET_KEY", "dev")
                      .getBytes(StandardCharsets.UTF_8)))
              .build()
              .parseSignedClaims(token)
              .getPayload()
              .getSubject();

      // проводит аутентификацию
      UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(payload, null, null);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      filterChain.doFilter(request, response);
    } catch (JwtException err) {
      response.getWriter().write(setError("Неправильный токен.", 401));
      response.setStatus(401);
    } catch (Exception err) {
      response.getWriter().write(setError("Непредвиденная ошибка.", 500));
      response.setStatus(500);
    }
  }

  // устанавливает ошибку
  private String setError(String text, int statusCode) throws JsonProcessingException {
    HandleErrorFields handleResponseFields = new HandleErrorFields();

    handleResponseFields.message = text;
    handleResponseFields.statusCode = statusCode;

    return new ObjectMapper().writeValueAsString(handleResponseFields);
  }

  private boolean getValidateIgnoredPath(HttpServletRequest request) {
    String[] paths = {"/api/signin", "/api/users/signup"};
    boolean response = false;

    for (String path : paths) {
      if (new AntPathRequestMatcher(path).matches(request)) {
        response = true;
      }
    }

    return response;
  }
}
