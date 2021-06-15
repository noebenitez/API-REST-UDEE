package com.utn.udee.service;

import com.utn.udee.exception.AddressNotExistsException;
import com.utn.udee.exception.UserExistsException;
import com.utn.udee.exception.UserNotExistsException;
import com.utn.udee.model.Address;
import com.utn.udee.model.Consumption;
import com.utn.udee.model.Measurement;
import com.utn.udee.model.User;
import com.utn.udee.model.dto.MeasurementDto;
import com.utn.udee.model.dto.UserDtoI;
import com.utn.udee.model.projections.UserProjection;
import com.utn.udee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MeasurementService measurementService;
    private final AddressService addressService;

    @Autowired
    public UserService(UserRepository userRepository,MeasurementService measurementService,AddressService addressService){
        this.userRepository = userRepository;
        this.addressService=addressService;
        this.measurementService=measurementService;
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

    public List<Measurement> getMeasurementsFromDateRange(Integer addressId, LocalDateTime from, LocalDateTime to) throws AddressNotExistsException {
        Address address = addressService.getById(addressId);
       return measurementService.getMeasurementsByAddressRangeDate(addressId,from,to);
    }

    public List<UserProjection> getTop10Consumers(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
       return measurementService.getTop10Consumers(from,to);
    }

    public List<Measurement> getRangeDateConsumption(Integer userId,LocalDateTime from, LocalDateTime to) {

        return measurementService.getRangeDateConsumption(userId,from,to);
    }

    public Consumption getTotalRangeDateConsumption(Integer userId, LocalDateTime from, LocalDateTime to) {

        return measurementService.getTotalRangeDateConsumption(userId,from,to);
    }
}
