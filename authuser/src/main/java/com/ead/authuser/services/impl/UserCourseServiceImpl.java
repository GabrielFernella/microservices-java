package com.ead.authuser.services.impl;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;

public class UserCourseServiceImpl implements UserCourseService {


    final UserCourseRepository userCourseRepository;

    public UserCourseServiceImpl (UserCourseRepository userCourseRepository) {
        this.userCourseRepository = userCourseRepository;
    }

}
