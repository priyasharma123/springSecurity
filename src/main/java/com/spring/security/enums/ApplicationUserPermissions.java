package com.spring.security.enums;

public enum ApplicationUserPermissions {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    ApplicationUserPermissions(String permission) {
        this.permission = permission;
    }

    private final String permission;

    public String getPermission() {
        return permission;
    }
}
