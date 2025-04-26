package com.ead.authuser.controllers;


import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.UserCourseDTO;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

    @Autowired
    CourseClient courseClient;

    @Autowired
    UserService userService;

    @Autowired
    UserCourseService userCourseService;

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllUCoursesByUser(
            @PathVariable(value = "userId") UUID userId,
            @PageableDefault(page = 0 , size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable
    ){
        var response = courseClient.getAllCoursesByUser(userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object>  saveSubscriptionUserInCourse(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Valid UserCourseDTO userCourseDTO
    ) {

        Optional<UserModel> user = userService.findById(userId);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if(userCourseService.existsByUserAndCourseId(user.get(), userCourseDTO.getCourseId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists!");
        }

        var buildUserCourseModel = user.get().convertToUserCourseModel(userCourseDTO.getCourseId());

        UserCourseModel userCourseModel = userCourseService.save(buildUserCourseModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }
}
