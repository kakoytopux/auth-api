package com.example.authapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.authapi.services.UserService;

/*
 * Не позволяй прошлому определять твое будущее.
 */

class CreateUserBody {
  public String email;
  public String password;
}

@RestController
@RequestMapping(
        path = "/api/users",
        produces = "application/json"
)
public class UserController {
  @PostMapping
  @RequestMapping("/signup")
  public ResponseEntity<String> createUser(@RequestBody CreateUserBody data) {
    return UserService.createUser(data.email, data.password);
  }

  @GetMapping
  public ResponseEntity<String> getUserInfo() {
    return UserService.getUserInfo();
  }
}
