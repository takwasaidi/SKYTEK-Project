package tn.esprit.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    INTERN_USER_READ("reservation:read"),
    INTERN_USER_UPDATE("reservation:update"),
    INTERN_USER_CREATE("reservation:create"),
    INTERN_USER_DELETE("reservation:delete"),
    EXTERN_USER_READ("reservation:read"),
    EXTERN_USER_UPDATE("reservation:update"),
    EXTERN_USER_CREATE("reservation:create"),
    EXTERN_USER_DELETE("reservation:delete")

    ;

    @Getter
    private final String permission;

}
