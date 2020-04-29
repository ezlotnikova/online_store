package com.gmail.ezlotnikova.service.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.gmail.ezlotnikova.service.model.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUser implements UserDetails {

    private final UserDTO user;
    private final List<SimpleGrantedAuthority> authorities;

    public AppUser(UserDTO user) {
        this.user = user;
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId(){
        return  user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return user.getEnabled();
    }

}