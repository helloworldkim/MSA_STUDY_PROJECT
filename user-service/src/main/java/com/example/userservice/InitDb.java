package com.example.userservice;


import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService {
        private final EntityManager em;
        private final UserService userService;
        public void dbInit1() {

            UserDto userDto = new UserDto();
            userDto.setName("helloworld");
            userDto.setEmail("abc@gmail.com");
            userDto.setPwd("test1234");
            UserDto user = userService.createUser(userDto);

            log.info("user.getUserId() = {}", user.getUserId());


        }





    }
}


