package com.utn.udee.service;

import com.utn.udee.exception.UserExistsException;
import com.utn.udee.model.User;
import com.utn.udee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
