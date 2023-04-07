package com.waltwang.maivc.security;

import java.util.Optional;
import com.waltwang.maivc.domain.Userm;
import com.waltwang.maivc.repository.UsermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsermRepository usermRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Userm> userm = usermRepository.findByUsername(username);
        UserBuilder builder = null;
        if(userm.isPresent()) {
            Userm currentUserm = userm.get();
            builder = User.withUsername(username)
                    .password(currentUserm.getPassword())
                    .roles(currentUserm.getRole());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
        return builder.build();
    }
}
