package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.backend.dto.user.UserDTO;
import tn.esprit.backend.entity.User;
import tn.esprit.backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUserInfo() {
        User user = userService.getCurrentUser();
       UserDTO dto = new UserDTO(user.getId(),  user.getFirstname(), user.getLastname(),user.getEmail(),user.getPhone(),user.getUser_type());
        return ResponseEntity.ok(dto);
    }


}
