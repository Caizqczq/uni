package com.unilife.service;

import com.unilife.mapper.UserMapper;
import com.unilife.model.entity.User;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

// @Service
public class UserDetailsServiceImpl /*implements UserDetailsService*/ {

    private final UserMapper userMapper;

    // @Autowired
    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // @Override
    public Object /*UserDetails*/ loadUserByUsername(String usernameOrEmail) /*throws UsernameNotFoundException*/ {
        User user = userMapper.findByUsername(usernameOrEmail);
        if (user == null) {
            user = userMapper.findByEmail(usernameOrEmail);
        }

        if (user == null) {
            // throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
            System.err.println("User not found with username or email: " + usernameOrEmail);
            return null; // Or throw exception
        }

        if (!user.isEnabled()) {
            // throw new UsernameNotFoundException("User account is not enabled: " + usernameOrEmail);
            System.err.println("User account is not enabled: " + usernameOrEmail);
            // Or throw specific exception like DisabledException if you want to handle it differently
            return null;
        }

        // Set<GrantedAuthority> authorities = user.getRoles().stream()
        //         .map(SimpleGrantedAuthority::new)
        //         .collect(Collectors.toSet());
        Set<Object> authorities = Collections.singleton(new Object() /*SimpleGrantedAuthority_USER*/);


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities);
    }
}
