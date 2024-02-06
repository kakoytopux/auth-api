package com.example.authapi.middlewares;

import com.example.authapi.utils.HandleErrorFields;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

/*
 * Не волнуйтесь, если что-то не работает. Если бы всё работало, вас бы уволили.
 */

// обрабатывает исключения
public class HandleException {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  // устанавливает и возвращает ошибку
  public static ResponseEntity<String> setException(String text, int statusCode) {
    HandleErrorFields HandleErrorFields = new HandleErrorFields();
    ResponseEntity<String> exception = null;

    try {
      HandleErrorFields.statusCode = statusCode;
      HandleErrorFields.message = text;

      exception = ResponseEntity.status(statusCode).body(OBJECT_MAPPER.writeValueAsString(HandleErrorFields));
    } catch (Exception err) {
      err.printStackTrace();
    }

    return exception;
  }
}
