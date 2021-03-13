package mx.simio.transactionsdemo.api.service.impl;

import mx.simio.transactionsdemo.api.exception.ResourceNotFoundException;
import mx.simio.transactionsdemo.api.model.entity.User;
import mx.simio.transactionsdemo.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static mx.simio.transactionsdemo.api.util.AppConstant.USER_NOT_FOUND;

import java.util.Optional;

public class UserServiceImplTest {
    private static final User USER = new User();
    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "Adrián";

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        USER.setId(USER_ID);
        USER.setName(USER_NAME);
    }

    @Test
    public void readUserByIdTest() {
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));
        userService.readUserById(USER_ID);
    }

    @Test
    public void readUserByIdNotFoundTest() {
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.empty()).thenThrow(new ResourceNotFoundException(USER_NOT_FOUND));
        assertThrows(ResourceNotFoundException.class, () -> userService.readUserById(USER_ID));
    }
}
