package com.utn.udee.service;

import com.utn.udee.exception.UserExistsException;
import com.utn.udee.exception.UserNotExistsException;
import com.utn.udee.model.User;
import com.utn.udee.model.dto.UserDtoI;
import com.utn.udee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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

    public Page<UserDtoI> getAll(Pageable pageable) {
        return userRepository.findAllDto(pageable);
    }

    public User getById(Integer id) throws UserNotExistsException {
        return userRepository.findById(id)
                .orElseThrow(UserNotExistsException::new);
    }

}
