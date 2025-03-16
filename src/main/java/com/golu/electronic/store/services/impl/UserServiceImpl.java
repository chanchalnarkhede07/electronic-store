package com.golu.electronic.store.services.impl;

import com.golu.electronic.store.dtos.PageableResponse;
import com.golu.electronic.store.dtos.UserDto;
import com.golu.electronic.store.entities.User;
import com.golu.electronic.store.exceptions.ResourceNotFoundException;
import com.golu.electronic.store.helper.Helper;
import com.golu.electronic.store.repositories.UserRepository;
import com.golu.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.profile.path}")
    private String fileUploadPath;

    @Override
    public UserDto createUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user = userDtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        return entityToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        //save user
        userRepository.save(user);
        return entityToUserDto(user);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //delete user image
        String fullPath = fileUploadPath + user.getImageName();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return entityToUserDto(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(
            int pageNumber, int pageSize, String sortField, String sortDirection
    ) {
//        Sort sort = Sort.by(sortField);
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? (Sort.by(sortField).descending()) : (Sort.by(sortField).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);
        List<User> users = page.getContent();
        List<UserDto> userDtoList = users.stream().map(user -> entityToUserDto(user)).collect(Collectors.toList());
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email"));
        return entityToUserDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> userDtoList = users.stream().map(user -> entityToUserDto(user)).collect(Collectors.toList());
        return userDtoList;
    }


    private UserDto entityToUserDto(User savedUser) {
//        return UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .imageName(savedUser.getImageName())
//                .gender(savedUser.getGender())
//                .about(savedUser.getAbout())
//                .build();
        return modelMapper.map(savedUser, UserDto.class);
    }

    private User userDtoToEntity(UserDto userDto) {
//        return User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .about(userDto.getAbout())
//                .email(userDto.getEmail())
//                .gender(userDto.getGender())
//                .password(userDto.getPassword())
//                .imageName(userDto.getImageName())
//                .build();
        return modelMapper.map(userDto, User.class);
    }
}
