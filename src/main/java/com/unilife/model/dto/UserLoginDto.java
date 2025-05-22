package com.unilife.model.dto;

// import javax.validation.constraints.NotBlank;

public class UserLoginDto {

    // @NotBlank(message = "Username or email cannot be blank")
    private String usernameOrEmail;

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
