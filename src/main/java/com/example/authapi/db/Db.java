package com.example.authapi.db;

import java.sql.*;

/*
  * Великие мечи не сделаны из металла, а из воли.
 */

// работа с бд
public class Db {

  // возвращает подключение к бд
  public static Connection getConnect() {
    // содержит подключение к бд
    Connection connect = null;

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      connect = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/auth",
              "root",
              "root"
      );
    } catch (Exception err) {
      err.printStackTrace();
    }

    return connect;
  }
}
