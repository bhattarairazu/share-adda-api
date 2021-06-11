package com.shareadda.api.ShareAdda.User.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoginRequestDto {
    private String username;
    private String password;

}
