package com.ead.course.services;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService  {

    LessonModel save(LessonModel lessonModel);

    void delete(LessonModel lessonModel);

    Optional<LessonModel> findById(UUID lessonId);

    List<LessonModel> findAllLessonsIntoModule(UUID moduleId);

    Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId);

    Page<LessonModel> findAllByModule(Specification<LessonModel> spec, Pageable pageable);
}
