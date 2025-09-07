package tn.esprit.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String phone;
    private String user_type;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;
    private boolean mfaEnabled;
    @Enumerated(EnumType.STRING)
    private MfaType mfaType;
    private String emailVerificationCode;
    private LocalDateTime codeExpiration;
    private LocalDateTime mfaTrustedUntil;

    private String secret;
    @ManyToOne
    private Entreprise entreprise ;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reservation;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reclamations;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
