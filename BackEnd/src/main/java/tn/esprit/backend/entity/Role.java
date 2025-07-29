package tn.esprit.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static tn.esprit.backend.entity.Permission.*;

@RequiredArgsConstructor
public enum Role {
    INTERN_USER(Set.of(
            INTERN_USER_READ,
            INTERN_USER_UPDATE,
            INTERN_USER_DELETE,
            INTERN_USER_CREATE
    )
    ),
    ADMIN(
            Set.of(
            ADMIN_READ,
            ADMIN_UPDATE,
            ADMIN_DELETE,
            ADMIN_CREATE,
                    INTERN_USER_READ,
                    INTERN_USER_UPDATE,
                    INTERN_USER_DELETE,
                    INTERN_USER_CREATE
            )
  ),
    EXTERN_USER(
            Set.of(
                    EXTERN_USER_READ,
                    EXTERN_USER_UPDATE,
                    EXTERN_USER_DELETE,
                    EXTERN_USER_CREATE
            )
  )

    ;

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
