package com.golu.electronic.store.controllers;

import com.golu.electronic.store.dtos.ApiResponseMessage;
import com.golu.electronic.store.dtos.ImageResponse;
import com.golu.electronic.store.dtos.PageableResponse;
import com.golu.electronic.store.dtos.UserDto;
import com.golu.electronic.store.services.FileService;
import com.golu.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.path}")
    private String fileUploadPath;

    //Create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto newUserDto = userService.createUser(userDto);
        return new ResponseEntity<>(newUserDto, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String userId,
                                              @Valid @RequestBody UserDto userDto) {
        UserDto userDto1 = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);
    }


    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User deleted Successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get all users
    @GetMapping
    public ResponseEntity<PageableResponse> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse userDtos = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    //single user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    //users by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> getSearchedUsers(@PathVariable String keywords) {
        return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
    }

    //upload image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image,
            @PathVariable String userId
    ) throws IOException {
        String imageName = fileService.uploadFile(image, fileUploadPath);
        UserDto userDto = userService.getUserById(userId);
        userDto.setImageName(imageName);
        userService.updateUser(userDto, userId);
        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("Image saved Successfully")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    //serve image
    @GetMapping("/image/{userId}")
    public void serveUserImage(
            @PathVariable String userId,
            HttpServletResponse response
    ) throws IOException {
        UserDto userDto = userService.getUserById(userId);
        InputStream resource = fileService.getFile(fileUploadPath, userDto.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
