package com.shareadda.api.ShareAdda.User.Service.impl;

import com.shareadda.api.ShareAdda.User.Domain.ERole;
import com.shareadda.api.ShareAdda.User.Domain.Role;
import com.shareadda.api.ShareAdda.User.Domain.User;
import com.shareadda.api.ShareAdda.User.Dto.JWTResponseDto;
import com.shareadda.api.ShareAdda.User.Dto.LoginRequestDto;
import com.shareadda.api.ShareAdda.User.Dto.SignupRequest;
import com.shareadda.api.ShareAdda.User.Repository.RoleRepository;
import com.shareadda.api.ShareAdda.User.Repository.UserRepository;
import com.shareadda.api.ShareAdda.User.Service.UserService;
import com.shareadda.api.ShareAdda.Utils.JWTUtils;
import com.shareadda.api.ShareAdda.exception.BackendException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTUtils jwtUtils;


    @Override
    public User saveUser(SignupRequest signupRequest) {
        //validate uesrname
        if(this.existsByEmail(signupRequest.getEmail()))
            throw new BackendException("User with email "+signupRequest.getEmail()+" Already Exists.Try a new one");

        if(this.existsByUsername(signupRequest.getUsername()))
            throw new BackendException("User with username "+signupRequest.getUsername()+" Already Exits.Try a new one");

        Set<String> strroles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strroles==null){
            throw new BackendException("At least one Role is Mandatory");
        }
        strroles.forEach(role->{
            switch (role){
                case "admin":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
                    roles.add(adminRole);
                    break;
                case "unpaiduser":
                    Role unpaidRole = roleRepository.findByName(ERole.ROLE_SIMPLEUSER);
                    roles.add(unpaidRole);
                    break;
                case "paiduser":
                    Role paidRole = roleRepository.findByName(ERole.ROLE_PAIDUSER);
                    roles.add(paidRole);
                    break;
                default:
                    throw new BackendException("Please Enter Valid Role");
            }
        });
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setUsername(signupRequest.getUsername());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setPhone(signupRequest.getPhone());
        user.setRoles(roles);



        return  userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public JWTResponseDto loginUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        JWTResponseDto jwtResponseDto = new JWTResponseDto();
        jwtResponseDto.setToken(jwt);
        return jwtResponseDto;
    }
}
