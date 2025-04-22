package com.ead.course.services.Impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Transactional //add para que caso ocorra algum erro, ele reverta todos os passos
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> modulesList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());

        if (!modulesList.isEmpty()) {
            for (ModuleModel module : modulesList) {

                List<LessonModel> lessonsList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessonsList.isEmpty()) {
                    lessonRepository.deleteAll(lessonsList);
                }
            }
            moduleRepository.deleteAll(modulesList);
        }

        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        courseRepository.save(courseModel);
        return courseModel;
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }
}
