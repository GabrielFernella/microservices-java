package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDTO;
import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class AuthUserClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    @Value("${ead.api.authuser}")
    String REQUEST_URL_AUTHUSER;

    public Page<UserDTO> getAllUsersByCourse(UUID courseId, Pageable pageable) {

        List<UserDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;

        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUsersByCourse(courseId, pageable);

        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);
        try {

            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType = new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>(){};
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            log.info("Response Status Code: {}", result);
            searchResult = result.getBody().getContent();

            log.debug("Response Number of Elements: {}", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request/users: {} ", e);
        }

        log.info("Ending request /users courseId: {}", courseId);
        return new PageImpl<>(searchResult);
    }


    public ResponseEntity<UserDTO> getOneUserById(UUID userId) {

        String url = REQUEST_URL_AUTHUSER + "/users/" + userId;

        log.debug("Request URL: {}", url);

        return restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);

    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/users/"+ userId +"/courses/subscription";

        var courseUserDto = new CourseUserDTO();
        courseUserDto.setCourseId(courseId);
        courseUserDto.setUserId(userId);

        restTemplate.postForObject(url, courseUserDto, String.class);



    }
}
