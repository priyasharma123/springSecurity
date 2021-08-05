package com.spring.security.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.spring.security.enums.ApplicationUserPermissions.*;

public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(STUDENT_READ,STUDENT_WRITE,COURSE_READ,COURSE_WRITE)),
    ADMINTRANIEE(Sets.newHashSet(STUDENT_READ,COURSE_READ));

    private final HashSet<ApplicationUserPermissions> permissions;

    ApplicationUserRole(HashSet<ApplicationUserPermissions> applicationUserPermissions) {
        this.permissions = applicationUserPermissions;
    }

    public HashSet<ApplicationUserPermissions> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
       Set<SimpleGrantedAuthority> permissions = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
       permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
       return permissions;
    }
}
