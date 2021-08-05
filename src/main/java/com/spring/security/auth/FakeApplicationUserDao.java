package com.spring.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.spring.security.enums.ApplicationUserRole.*;

@Repository("fake")
public class FakeApplicationUserDao implements  ApplicationUserDao {
    private  final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDao(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationByUsername(String username) {
        return getAppplicationUsers()
                .stream()
                .filter(applicationUser -> applicationUser.getUsername().equals(username))
                .findFirst();
    }

    private List<ApplicationUser> getAppplicationUsers(){
        List<ApplicationUser> applicationUsers = Arrays.asList(
                new ApplicationUser("priya",
                        passwordEncoder.encode("pass"),
                        STUDENT.getGrantedAuthorities(),
                        true,true,true,true),
                new ApplicationUser("nikhil",
                        passwordEncoder.encode("pass"),
                        ADMIN.getGrantedAuthorities(),
                        true,true,true,true),
                new ApplicationUser("shubham",
                        passwordEncoder.encode("pass"),
                        ADMINTRANIEE.getGrantedAuthorities(),
                        true,true,true,true)

        );
        return applicationUsers;
    }

}
