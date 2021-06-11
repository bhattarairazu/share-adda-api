package com.shareadda.api.ShareAdda.User.Service;



import com.shareadda.api.ShareAdda.User.Domain.User;
import com.shareadda.api.ShareAdda.User.Dto.JWTResponseDto;
import com.shareadda.api.ShareAdda.User.Dto.LoginRequestDto;
import com.shareadda.api.ShareAdda.User.Dto.SignupRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(SignupRequest signupRequest);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    List<User> findAll();

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    JWTResponseDto loginUser(LoginRequestDto loginRequestDto);
}
