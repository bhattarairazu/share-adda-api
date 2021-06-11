package com.shareadda.api.ShareAdda.User.Domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shareadda.api.ShareAdda.audititing.Auditing;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class User extends Auditing implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @TextIndexed
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @TextIndexed
    private String firstName;
    private String lastName;

    @TextIndexed
    @NotBlank
    @Size(max = 20)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(max = 120)
    private String password;

    @TextIndexed
    @NotBlank
    @Size(min = 10,max = 10)
    private String phone;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    private Boolean firstLogin = true;

    private String image;
}
