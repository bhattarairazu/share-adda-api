package com.shareadda.api.ShareAdda.User.Dto;

import com.shareadda.api.ShareAdda.User.Domain.Role;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class JWTResponseDto {
    private String username;
    private String useremail;
    private String fullname;
    private String userid;
    private String phone;
    private Set<Role> roles  = new HashSet<>();
    private String token;
}
