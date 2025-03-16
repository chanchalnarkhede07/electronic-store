package com.golu.electronic.store.dtos;


import com.golu.electronic.store.validation.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @Size(min = 3, max = 50, message = "Invalid name !!")
    private String name;

    @NotBlank(message = "Password is required !!")
    private String password;

//    @Email(message = "Invalid name !!")
    @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "Invalid email")
    @NotBlank(message = "Email is required!!")
    private String email;

    @Size(min = 2, max = 5, message = "Invalid Gender!!")
    private String gender;

    private String about;

    @ImageNameValid
    private String imageName;
}
