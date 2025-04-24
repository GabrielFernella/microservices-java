package com.ead.course.services;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface CourseUserService {
    boolean existsByCourseAndUserId(CourseModel courseModel, @NotNull UUID userId);

    CourseUserModel save(CourseUserModel courseUserModel);
}
