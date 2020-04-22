package com.gmail.ezlotnikova.web.runner;

import com.gmail.ezlotnikova.repository.model.сonstant.UserRoleEnum;
import com.gmail.ezlotnikova.service.UserService;
import com.gmail.ezlotnikova.service.model.AddUserDTO;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements ApplicationRunner {

    private final UserService userService;

    public Runner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) {
        AddUserDTO newUser1 = new AddUserDTO();
        newUser1.setLastName("Ivanov");
        newUser1.setFirstName("Ivan");
        newUser1.setPatronymicName("Ivanovich");
        newUser1.setEmail("ii@shop.test");
        newUser1.setRole(UserRoleEnum.ADMINISTRATOR);
        AddUserDTO addedUserDTO1 = userService.add(newUser1);

        AddUserDTO newUser2 = new AddUserDTO();
        newUser2.setLastName("Petrov");
        newUser2.setFirstName("Petr");
        newUser2.setPatronymicName("Petrovich");
        newUser2.setEmail("pp@shop.test");
        newUser2.setRole(UserRoleEnum.CUSTOMER_USER);
        AddUserDTO addedUserDTO2 = userService.add(newUser2);

        AddUserDTO newUser3 = new AddUserDTO();
        newUser2.setLastName("Sidorov");
        newUser2.setFirstName("Sidor");
        newUser2.setPatronymicName("Sidorovich");
        newUser2.setEmail("ss@shop.test");
        newUser2.setRole(UserRoleEnum.SALE_USER);
        AddUserDTO addedUserDTO3 = userService.add(newUser2);

        AddUserDTO newUser4 = new AddUserDTO();
        newUser2.setLastName("Borisov");
        newUser2.setFirstName("Boris");
        newUser2.setPatronymicName("Borisovich");
        newUser2.setEmail("bb@shop.test");
        newUser2.setRole(UserRoleEnum.SECURE_API_USER);
        AddUserDTO addedUserDTO4 = userService.add(newUser2);
        //
        //        AddUserDTO newUser5 = new AddUserDTO();
        //        newUser2.setLastName("Alekseev");
        //        newUser2.setFirstName("Aleksei");
        //        newUser2.setPatronymicName("Alekseevich");
        //        newUser2.setEmail("aa@shop.test");
        //        newUser2.setRole(UserRoleEnum.ADMINISTRATOR);
        //        AddUserDTO addedUserDTO5 = userService.add(newUser2);
    }

}