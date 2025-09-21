package docshare.com.service.services.impl;

import docshare.com.service.exception.DuplicateEmailException;
import docshare.com.service.model.User;
import docshare.com.service.repository.UserRepository;
import docshare.com.service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new DuplicateEmailException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long userid) {
        return userRepository.findById(userid);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public String deleteUser(Long Id) {
        Optional<User> user = getUserById(Id);
        if(user.isPresent()){
            userRepository.delete(user.get());
            return "success";
        }
        throw new RuntimeException("User not found");
    }
}
