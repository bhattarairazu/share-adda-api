package com.shareadda.api.ShareAdda.User.Dto;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SignupRequest {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private String firstName;
    private String lastName;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(min = 10,max = 10)
    private String phone;

    @DBRef
    private Set<String> roles;

}
