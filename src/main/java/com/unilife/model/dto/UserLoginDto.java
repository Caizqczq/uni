package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// import javax.validation.constraints.NotBlank;

@Schema(description = "Data Transfer Object for user login.")
public class UserLoginDto {

    @Schema(description = "Username or email address of the user.", example = "john.doe", requiredMode = Schema.RequiredMode.REQUIRED)
    // @NotBlank(message = "Username or email cannot be blank")
    private String usernameOrEmail;

    @Schema(description = "Password for the account.", example = "P@$$wOrd", requiredMode = Schema.RequiredMode.REQUIRED)
    // @NotBlank(message = "Password cannot be blank")
    private String password;

    // Getters and Setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLoginDto{" +
                "usernameOrEmail='" + usernameOrEmail + '\'' +
                '}';
    }
}
