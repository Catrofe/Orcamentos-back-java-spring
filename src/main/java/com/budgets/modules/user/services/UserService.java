package com.budgets.modules.user.services;

import com.budgets.modules.user.dto.EditUserDTO;
import com.budgets.modules.user.dto.UserLoginDTO;
import com.budgets.modules.user.dto.UserOutputDTO;
import com.budgets.modules.user.dto.UserRegisterDTO;
import com.budgets.modules.user.entities.User;
import com.budgets.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder encoder;

    public UserRegisterDTO insert(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user = repository.save(user);
        return new UserRegisterDTO(user.getId(), user.getEmail());
    }

    public UserRegisterDTO findByUser(UserLoginDTO user) {
        User findByEmail = Optional.ofNullable(repository.findByEmail(user.getEmail())).orElseThrow(() -> new UsernameNotFoundException("Email or Password Not Found"));
        if (encoder.matches(user.getPassword(), findByEmail.getPassword())) {
            return new UserRegisterDTO(findByEmail.getId(), findByEmail.getEmail());
        }
        else {
            throw new UsernameNotFoundException("Email or Password Not Found");
        }
    }

    public UserOutputDTO findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return new UserOutputDTO(user.getId(), user.getName(), user.getEmail(), user.getCpf(), user.getPhone());
    }

    public UserOutputDTO update(Long id, EditUserDTO user) {
        User userUpdate = repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        if (user.getName() != null) {
            userUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userUpdate.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            userUpdate.setPhone(user.getPhone());
        }
        if (user.getPassword() != null) {
            userUpdate.setPassword(encoder.encode(user.getPassword()));
        }
        userUpdate = repository.save(userUpdate);
        return new UserOutputDTO(userUpdate.getId(), userUpdate.getName(), userUpdate.getEmail(), userUpdate.getCpf(), userUpdate.getPhone());
    }
}
