package com.ead.authuser.controllers;


import com.ead.authuser.dtos.InstructorDTO;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    final UserService userService;

    public InstructorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(
            @RequestBody @Valid InstructorDTO instructorDTO
    ) {

        Optional<UserModel> userModelOptional = userService.findById(instructorDTO.getUserId());

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            var userModel = userModelOptional.get();

            if (userModel.getUserType().equals(UserType.INSTRUCTOR)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already is an instructor");
            }

            userModel.setUserType(UserType.INSTRUCTOR);
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);

            log.info("Instructor saved successfully userId: {}", userModel.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
        }
    }


}
