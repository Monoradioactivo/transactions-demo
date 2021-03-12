package mx.simio.transactionsdemo.api.service.impl;

import mx.simio.transactionsdemo.api.exception.ResourceNotFoundException;
import mx.simio.transactionsdemo.api.model.entity.User;
import mx.simio.transactionsdemo.api.repository.UserRepository;
import mx.simio.transactionsdemo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User readUserById(Long id) {
        Optional<User> userFromDb = userRepository.findById(id);

        if (userFromDb.isEmpty()) {
            throw new ResourceNotFoundException("User was not found");
        }

        return userFromDb.get();
    }
}
