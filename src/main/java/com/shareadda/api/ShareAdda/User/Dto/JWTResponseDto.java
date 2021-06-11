package com.shareadda.api.ShareAdda.User.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class JWTResponseDto {
    private String token;
}
