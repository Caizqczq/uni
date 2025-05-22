package com.unilife.model.dto;

// import javax.validation.constraints.Email;
// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.Size;

public class UserRegistrationDto {

    // @NotBlank(message = "Username cannot be blank")
    // @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    // @NotBlank(message = "Password cannot be blank")
    // @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    // @NotBlank(message = "Email cannot be blank")
    // @Email(message = "Email should be valid")
    private String email;

    private String nickname;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
