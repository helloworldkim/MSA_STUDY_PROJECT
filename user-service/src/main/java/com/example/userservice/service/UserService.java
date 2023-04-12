package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final OrderServiceClient orderServiceClient;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(username);
        if (!optionalUserEntity.isPresent()) {
            throw new UsernameNotFoundException(username);
        }

        UserEntity userEntity = optionalUserEntity.get();
        return new User(userEntity.getEmail()
                , userEntity.getEncryptedPwd()
                , true
                , true
                , true
                , true
                , new ArrayList<>());
    }

    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = objectMapper.convertValue(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        UserDto returnUserdto = objectMapper.convertValue(userEntity, UserDto.class);
        return returnUserdto;
    }

    public UserDto getUserByUserIdByResttemplate(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow();
        UserDto userDto = objectMapper.convertValue(userEntity, UserDto.class);
        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
        ResponseEntity<List<ResponseOrder>> orderListResponse =
                restTemplate.exchange(
                        orderUrl
                        , HttpMethod.GET
                        , null
                        , new ParameterizedTypeReference<List<ResponseOrder>>() {
                        });

        List<ResponseOrder> orderList = orderListResponse.getBody();
        userDto.setOrders(orderList);
        return userDto;
    }

    public UserDto getUserByUserIdByFeginClient(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow();
        UserDto userDto = objectMapper.convertValue(userEntity, UserDto.class);
        List<ResponseOrder> orderList = null;
        try {
            orderList = orderServiceClient.getOrders(userId);
        } catch (FeignException ex) {
            log.error(ex.getMessage());
        }
        userDto.setOrders(orderList);
        return userDto;
    }

    public List<UserEntity> getUserAll() {
        return userRepository.findAll();
    }

    public UserDto getUserDetailsByEmail(String username) {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow();

        return objectMapper.convertValue(userEntity, UserDto.class);

    }
}
