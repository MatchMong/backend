package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User implements UserDetails{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id",updatable = false)
        private Long id;

        @Column(name = "email", nullable = false, unique = true)
        private String email;

        @Column(name = "password", nullable = false)
        private String password;

        @Column(name = "discord_id")
        private String discordId;

        @Column(name = "major")
        private String major;

        @Column(name = "selfwrite")
        private String selfwrite;


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

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public String getNickname() {
        return null;
    }
}

