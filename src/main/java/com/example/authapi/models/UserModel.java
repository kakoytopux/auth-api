package com.example.authapi.models;

import com.example.authapi.db.Db;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;

/*
 * Жизнь это битва, жизнь это борьба. Тот, кто не в состоянии бороться, не заслуживает жить.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
class UserInfo {
  public String id;
  public String email;

  public String password;
}

// модель пользователя
public class UserModel {

  // содержит подключение к бд
  private static final Connection CONNECT = Db.getConnect();

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  // создает сущность пользователя
  public static String createUser(String email, String password) throws Exception {
    String response = null;

    // содержит запрос к бд
    PreparedStatement statementCreate = CONNECT.prepareStatement(
            "INSERT INTO user SET email=?, password=?;"
    );

    PreparedStatement statementGetId = CONNECT.prepareStatement(
            "SELECT LAST_INSERT_ID() AS id;"
    );

    statementCreate.setString(1, email);
    statementCreate.setString(2, password);

    statementCreate.executeUpdate();
    ResultSet resultSet = statementGetId.executeQuery();

    while (resultSet.next()) {
      response = resultSet.getString("id");
    }

    return response;
  }

  // возвращает данные пользователя по id
  public static String findById(String id) throws Exception {
    UserInfo userInfo = new UserInfo();

    // содержит запрос к бд
    PreparedStatement statement = CONNECT.prepareStatement(
            "SELECT * FROM user WHERE id=?;"
    );

    statement.setString(1, id);
    ResultSet resultSet = statement.executeQuery();

    while (resultSet.next()) {
      String userId = resultSet.getString("id");
      String userEmail = resultSet.getString("email");

      userInfo.id = userId;
      userInfo.email = userEmail;
    }

    return OBJECT_MAPPER.writeValueAsString(userInfo);
  }

  // возвращает данные пользователя по email
  public static String findByEmail(String email) throws Exception {
    UserInfo userInfo = new UserInfo();

    // содержит запрос к бд
    PreparedStatement statement = CONNECT.prepareStatement(
            "SELECT * FROM user WHERE email=?;"
    );

    statement.setString(1, email);
    ResultSet resultSet = statement.executeQuery();

    while (resultSet.next()) {
      String userId = resultSet.getString("id");
      String userEmail = resultSet.getString("email");
      String userPassword = resultSet.getString("password");

      userInfo.id = userId;
      userInfo.email = userEmail;
      userInfo.password = userPassword;
    }

    return OBJECT_MAPPER.writeValueAsString(userInfo);
  }
}
