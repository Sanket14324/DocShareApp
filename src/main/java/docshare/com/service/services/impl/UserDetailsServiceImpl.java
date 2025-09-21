package docshare.com.service.services.impl;

import docshare.com.service.model.User;
import docshare.com.service.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userServiceImpl.getUserByEmail(email);
        return userOptional.map(UserDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + email));
    }
}
