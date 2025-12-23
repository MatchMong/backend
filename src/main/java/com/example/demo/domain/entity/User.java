package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails{
        @Id
        @GeneratedValue
        @Column(name = "id",updatable = false)
        private Long id;

        @Column(name = "email", nullable = false, unique = true)
        private String email;

        @Column(name = "password")
        private String password;

        @Column(name = "major")
        private String major;

        @Builder
        public User(String email, String password, String auth){
            this.email = email;
            this.password = password;
            this.major = major;
        }

    public void updateMajor(String major) {
        this.major = major;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
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

