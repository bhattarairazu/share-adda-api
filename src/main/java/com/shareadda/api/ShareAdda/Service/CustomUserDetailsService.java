package com.shareadda.api.ShareAdda.Service;

import com.shareadda.api.ShareAdda.User.Domain.User;
import com.shareadda.api.ShareAdda.User.Repository.UserRepository;
import com.shareadda.api.ShareAdda.User.Service.impl.UserDetailsImpl;
import com.shareadda.api.ShareAdda.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(()-> new ResourceNotFoundException("User with username "+username+" Not found"));

        return UserDetailsImpl.build(user);
    }
//    @Transactional
//    public User loadUserById(String id){
//        User user = userRepository.getById(id);
//        if(user==null) throw new ResourceNotFoundException("User not found");
//        return user;
//    }

//    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(User user) {
//        List<GrantedAuthority> authorities = user.getRoles().stream().
//                map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//        return authorities;
//    }

}
