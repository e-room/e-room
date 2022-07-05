package io.swagger.api;

import io.swagger.model.Building;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-05T06:53:15.751Z[GMT]")
@RestController
public class BuildingsApiController implements BuildingsApi {

    private static final Logger log = LoggerFactory.getLogger(BuildingsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public BuildingsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Building> getBuildingById(@Parameter(in = ParameterIn.PATH, description = "빌딩의 numeric ID", required=true, schema=@Schema()) @PathVariable("buildingId") Integer buildingId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Building>(objectMapper.readValue("{\n  \"owner\" : [ null, null ],\n  \"id\" : 0,\n  \"roomList\" : [ {\n    \"reviewList\" : [ {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    }, {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    } ],\n    \"memberList\" : [ {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    }, {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    } ],\n    \"id\" : 6\n  }, {\n    \"reviewList\" : [ {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    }, {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    } ],\n    \"memberList\" : [ {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    }, {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    } ],\n    \"id\" : 6\n  } ]\n}", Building.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Building>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Building>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Object> registerBuilding(@Parameter(in = ParameterIn.PATH, description = "빌딩의 numeric ID", required=true, schema=@Schema()) @PathVariable("buildingId") Integer buildingId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Object>(objectMapper.readValue("{ }", Object.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Object> updateBuilding(@Parameter(in = ParameterIn.PATH, description = "빌딩의 numeric ID", required=true, schema=@Schema()) @PathVariable("buildingId") Integer buildingId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Object>(objectMapper.readValue("{ }", Object.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
    }

}
