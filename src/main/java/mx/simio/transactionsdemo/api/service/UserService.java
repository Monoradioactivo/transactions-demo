package mx.simio.transactionsdemo.api.service;

import mx.simio.transactionsdemo.api.model.entity.User;

public interface UserService {
    User readUserById(Long id);
}
