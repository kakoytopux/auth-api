package com.example.authapi.services;

import com.example.authapi.middlewares.HandleException;
import com.example.authapi.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/*
 * Хватит жить ниже своего потенциала.
 */

class JsonWebToken {
  public String jwt;
}

@Service
public class SignInService {

  // осуществляет вход
  public static ResponseEntity<String> login(String email, String password) {
    ResponseEntity<String> response;
    JsonWebToken jsonWebToken = new JsonWebToken();
    Dotenv dotenv = Dotenv.configure().load();

    try {
      // получает данные пользователя по email
      String userData = UserRepository.getUserInfoByEmail(email);
      final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

      // читает полученный json
      JsonNode json = OBJECT_MAPPER.readTree(userData);

      // проверка на совпадение с email и паролем
      if (!json.path("email").asText().equals("null")) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(password, json.path("password").asText())) {
          // генерация jwt
          jsonWebToken.jwt = Jwts.builder()
                  .subject(json.path("id").asText())
                  .signWith(Keys.hmacShaKeyFor(dotenv.get("SECRET_KEY", "dev").getBytes(StandardCharsets.UTF_8)))
                  .compact();

          response = ResponseEntity.status(200).body(OBJECT_MAPPER.writeValueAsString(jsonWebToken));
        } else {
          response = HandleException.setException("Неправильный email или пароль.", 401);
        }
      } else {
        response = HandleException.setException("Неправильный email или пароль.", 401);
      }
    } catch (Exception err) {
      response = HandleException.setException("Непредвиденная ошибка.", 500);
    }

    return response;
  }
}
