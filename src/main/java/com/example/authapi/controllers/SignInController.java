package com.example.authapi.controllers;

import com.example.authapi.services.SignInService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * В моем словаре нет слова «невозможно».
 */

class LoginBody {
  public String email;
  public String password;
}

@RestController
@RequestMapping(
        path = "/api/signin",
        produces = "application/json"
)
public class SignInController {
  @PostMapping
  public ResponseEntity<String> login(@RequestBody LoginBody data) {
    return SignInService.login(data.email, data.password);
  }
}
