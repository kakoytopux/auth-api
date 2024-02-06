package com.example.authapi.services;

import com.example.authapi.middlewares.HandleException;
import com.example.authapi.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

/*
 * Чтобы победить монстра, ты должен стать монстром.
 */

@Service
public class UserService {
  private static ResponseEntity<String> response;


  // создание пользователя
  public static ResponseEntity<String> createUser(String email, String password) {
    String id;

    try {
      // шифрование пароля
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
      String encodePassword = encoder.encode(password);

      id = UserRepository.createUser(email, encodePassword);

      // получение данных созданного пользователя
      response = ResponseEntity.status(201).body(UserRepository.getUserInfoById(id));
    } catch (SQLIntegrityConstraintViolationException err) {
      response = HandleException.setException("Такая почта уже используется.", 409);
    } catch (Exception err) {
      response = HandleException.setException("Непредвиденная ошибка.", 500);
    }

    return response;
  }

  // получение данных пользователя по id
  public static ResponseEntity<String> getUserInfo() {

    try {
      response = ResponseEntity
                      .status(200)
                      .body(UserRepository.getUserInfoById((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    } catch (Exception err) {
      response = HandleException.setException("Непредвиденная ошибка.", 500);
    }

    return response;
  }
}
