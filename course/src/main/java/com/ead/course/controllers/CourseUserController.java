package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    AuthUserClient authUserClient;

    @Autowired
    CourseService courseService;

    @Autowired
    CourseUserService courseUserService;


    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUserByCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @PageableDefault(page = 0 , size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable
    ){
        var response = authUserClient.getAllUsersByCourse(courseId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionDTO subscriptionDTO
    ){

        ResponseEntity<UserDTO> responseUser;

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        if(courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDTO.getUserId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists.");
        }

        // fazer uma verificação de user
        try {
            responseUser = authUserClient.getOneUserById(subscriptionDTO.getUserId());
            if(responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User is blocked.");
            }
        } catch (HttpStatusCodeException e) {
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        }

        CourseUserModel courseUserModel = courseUserService.saveAndSendSubscriptionUserInCourse(courseModelOptional.get().convertToCourseUserModel(subscriptionDTO.getUserId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }

}
