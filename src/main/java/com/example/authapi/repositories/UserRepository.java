package com.example.authapi.repositories;

import com.example.authapi.models.UserModel;
import org.springframework.stereotype.Repository;

/*
 * В конечном счете, сила человека находится в его душе.
 */

// репозиторий для взаимодействия с моделью пользователя
@Repository
public class UserRepository {
  public static String createUser(String email, String password) throws Exception {
    return UserModel.createUser(email, password);
  }

  public static String getUserInfoById(String id) throws Exception {
    return UserModel.findById(id);
  }

  public static String getUserInfoByEmail(String email) throws Exception {
    return UserModel.findByEmail(email);
  }
}
