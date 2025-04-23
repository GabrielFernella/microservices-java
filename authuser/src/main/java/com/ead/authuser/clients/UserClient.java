package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import com.ead.authuser.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class UserClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {

        List<CourseDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;

        String url = utilsService.createUrl(userId, pageable);

        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);
        try {

            ParameterizedTypeReference<ResponsePageDTO<CourseDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>(){};

            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            log.info("Response Status Code: {}", result);
            searchResult = result.getBody().getContent();

            log.debug("Response Number of Elements: {}", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request/courses: {} ", e);
        }

        log.info("Ending request /courses userId: {}", userId);
        return new PageImpl<>(searchResult);
    }
}
