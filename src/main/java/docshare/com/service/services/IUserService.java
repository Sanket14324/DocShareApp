package docshare.com.service.services;

import docshare.com.service.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User registerUser(User user);

    Optional<User>  getUserById(Long user);

    Optional<User> getUserByEmail(String email);

    List<User> getUserList();

    String deleteUser(Long Id);

}
