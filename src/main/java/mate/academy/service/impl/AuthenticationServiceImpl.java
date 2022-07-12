package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userFromDbOptional = userService.findByEmail(email);
        User user = userFromDbOptional.get();
        String hashedPassword = HashUtil.hashPassword(password, user.getSalt());
        if (!userFromDbOptional.isEmpty() && user.getPassword().equals(hashedPassword)) {
            return user;
        }
        throw new AuthenticationException("Login or password doesn't correct");
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        User user = new User();
        Optional<User> userFromDbOptional = userService.findByEmail(email);
        if (userFromDbOptional.isEmpty()) {
            user.setEmail(email);
            user.setPassword(password);
            userService.add(user);
            return user;
        }
        throw new RegistrationException("User with email " + email + " already exist");
    }
}
