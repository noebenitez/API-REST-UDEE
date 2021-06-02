package com.utn.udee.service;

import com.utn.udee.exception.UserExistsException;
import com.utn.udee.exception.UserNotExistsException;
import com.utn.udee.model.User;
import com.utn.udee.model.UserType;
import com.utn.udee.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.DecodingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User add(User user) throws UserExistsException {
        if(!userRepository.existsByDni(user.getDni())){
            return userRepository.save(user);
        }else{
            throw new UserExistsException();
        }
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getById(Integer id) throws UserNotExistsException {
        return userRepository.findById(id)
                .orElseThrow(UserNotExistsException::new);
    }

}
