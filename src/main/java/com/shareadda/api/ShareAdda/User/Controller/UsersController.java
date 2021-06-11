package com.shareadda.api.ShareAdda.User.Controller;

import com.shareadda.api.ShareAdda.User.Dto.LoginRequestDto;
import com.shareadda.api.ShareAdda.User.Dto.SignupRequest;
import com.shareadda.api.ShareAdda.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        return new ResponseEntity<>(userService.saveUser(signupRequest), HttpStatus.CREATED);

    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return new ResponseEntity<>(userService.loginUser(loginRequestDto), HttpStatus.OK);
    }

}

