package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.auth.AuthenticationResponse;
import tn.esprit.backend.dto.auth.RegisterRequest;
import tn.esprit.backend.dto.user.UserDTO;
import tn.esprit.backend.dto.user.UserQuotaDTO;
import tn.esprit.backend.entity.User;
import tn.esprit.backend.mappers.EntrepriseMapper;
import tn.esprit.backend.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

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
       UserDTO dto = new UserDTO(user.getId()
               ,  user.getFirstname()
               , user.getLastname()
               ,user.getEmail()
               ,user.getPhone()
               ,user.getUser_type(),
               EntrepriseMapper.toDto(user.getEntreprise()));
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/createUserFromAdmin")
    public ResponseEntity<AuthenticationResponse> createUserFromAdmin(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = userService.createUserFromAdmin(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users-over-quota/{entrepriseId}")
    public List<UserQuotaDTO> getUsersOverQuota(@PathVariable Integer entrepriseId) {
        return userService.getUsersOverQuota(entrepriseId);
    }
}
