package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> , JpaSpecificationExecutor<LessonModel> {

    @Query(value = "select * from TB_LESSONS where module_module_id = :moduleId", nativeQuery = true)
    List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId);

    @Query(value = "select * from TB_LESSONS where module_module_id = :moduleId AND lesson_id = :lessonId", nativeQuery = true)
    LessonModel findLessonIntoModule(@Param("moduleId") UUID moduleId, @Param("lessonId") UUID lessonId);


}
