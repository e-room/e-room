package io.swagger.api;

import io.swagger.model.Building;
import io.swagger.model.Review;
import io.swagger.model.Room;
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
public class BuildingApiController implements BuildingApi {

    private static final Logger log = LoggerFactory.getLogger(BuildingApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public BuildingApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Review> deleteReview(@Parameter(in = ParameterIn.PATH, description = "리뷰의 numeric Id", required=true, schema=@Schema()) @PathVariable("reviewId") Integer reviewId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Review>(objectMapper.readValue("{\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n}", Review.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Review>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Review>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Review> getReviewByReviewId(@Parameter(in = ParameterIn.PATH, description = "리뷰의 numeric Id", required=true, schema=@Schema()) @PathVariable("reviewId") Integer reviewId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Review>(objectMapper.readValue("{\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n}", Review.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Review>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Review>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Room> getRoomByRoomId(@Parameter(in = ParameterIn.PATH, description = "호실의 numeric Id", required=true, schema=@Schema()) @PathVariable("roomId") Integer roomId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Room>(objectMapper.readValue("{\n  \"reviewList\" : [ {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  }, {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  } ],\n  \"memberList\" : [ {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  }, {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  } ],\n  \"id\" : 6\n}", Room.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Room>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Room>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Review> registerReview(@Parameter(in = ParameterIn.PATH, description = "리뷰의 numeric Id", required=true, schema=@Schema()) @PathVariable("reviewId") Integer reviewId,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Review>(objectMapper.readValue("{\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n}", Review.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Review>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Review>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> registerRoomsInBuilding(@Parameter(in = ParameterIn.PATH, description = "빌딩의 numeric ID", required=true, schema=@Schema()) @PathVariable("buildingId") Integer buildingId,@Parameter(in = ParameterIn.DEFAULT, description = "호실 정보를 추가하기 위한 roomsList", required=true, schema=@Schema()) @Valid @RequestBody List<Room> body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Building>> searchBuildings(@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "locationRange", required = false) Object locationRange,@Parameter(in = ParameterIn.QUERY, description = "직거래 가능한지" ,schema=@Schema()) @Valid @RequestParam(value = "direct", required = false) Boolean direct) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Building>>(objectMapper.readValue("[ {\n  \"owner\" : [ null, null ],\n  \"id\" : 0,\n  \"roomList\" : [ {\n    \"reviewList\" : [ {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    }, {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    } ],\n    \"memberList\" : [ {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    }, {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    } ],\n    \"id\" : 6\n  }, {\n    \"reviewList\" : [ {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    }, {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    } ],\n    \"memberList\" : [ {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    }, {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    } ],\n    \"id\" : 6\n  } ]\n}, {\n  \"owner\" : [ null, null ],\n  \"id\" : 0,\n  \"roomList\" : [ {\n    \"reviewList\" : [ {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    }, {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    } ],\n    \"memberList\" : [ {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    }, {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    } ],\n    \"id\" : 6\n  }, {\n    \"reviewList\" : [ {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    }, {\n      \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n      \"reviewForm\" : {\n        \"id\" : 5\n      },\n      \"anonymous\" : {\n        \"isAnonymous\" : true,\n        \"anonymousName\" : \"anonymousName\"\n      },\n      \"id\" : 1\n    } ],\n    \"memberList\" : [ {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    }, {\n      \"name\" : \"name\",\n      \"id\" : 5,\n      \"userId\" : 2,\n      \"profileImageUrl\" : \"profileImageUrl\",\n      \"email\" : \"email\",\n      \"memberRole\" : \"user\"\n    } ],\n    \"id\" : 6\n  } ]\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Building>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Building>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Review>> searchReviewByRoom(@Parameter(in = ParameterIn.PATH, description = "호실의 numeric Id", required=true, schema=@Schema()) @PathVariable("roomId") Integer roomId,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orderByDate", required = false) Boolean orderByDate,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orderByRating", required = false) Boolean orderByRating) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Review>>(objectMapper.readValue("[ {\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n}, {\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Review>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Review>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Review>> searchReviews(@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orderByDate", required = false) Boolean orderByDate,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orderByRating", required = false) Boolean orderByRating) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Review>>(objectMapper.readValue("[ {\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n}, {\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Review>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Review>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Review>> searchReviewsByBuilding(@Parameter(in = ParameterIn.PATH, description = "빌딩의 numeric Id", required=true, schema=@Schema()) @PathVariable("buildingId") Integer buildingId,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orderByDate", required = false) Boolean orderByDate,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orderByRating", required = false) Boolean orderByRating) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Review>>(objectMapper.readValue("[ {\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n}, {\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Review>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Review>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Room>> searchRooms(@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "locationRange", required = false) Object locationRange,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orderByDate", required = false) Boolean orderByDate,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "orderByRating", required = false) Boolean orderByRating) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Room>>(objectMapper.readValue("[ {\n  \"reviewList\" : [ {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  }, {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  } ],\n  \"memberList\" : [ {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  }, {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  } ],\n  \"id\" : 6\n}, {\n  \"reviewList\" : [ {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  }, {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  } ],\n  \"memberList\" : [ {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  }, {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  } ],\n  \"id\" : 6\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Room>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Room>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Room>> searchRoomsByBuilding(@Parameter(in = ParameterIn.PATH, description = "빌딩의 numeric ID", required=true, schema=@Schema()) @PathVariable("buildingId") Integer buildingId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Room>>(objectMapper.readValue("[ {\n  \"reviewList\" : [ {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  }, {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  } ],\n  \"memberList\" : [ {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  }, {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  } ],\n  \"id\" : 6\n}, {\n  \"reviewList\" : [ {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  }, {\n    \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n    \"reviewForm\" : {\n      \"id\" : 5\n    },\n    \"anonymous\" : {\n      \"isAnonymous\" : true,\n      \"anonymousName\" : \"anonymousName\"\n    },\n    \"id\" : 1\n  } ],\n  \"memberList\" : [ {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  }, {\n    \"name\" : \"name\",\n    \"id\" : 5,\n    \"userId\" : 2,\n    \"profileImageUrl\" : \"profileImageUrl\",\n    \"email\" : \"email\",\n    \"memberRole\" : \"user\"\n  } ],\n  \"id\" : 6\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Room>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Room>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Review> updateReview(@Parameter(in = ParameterIn.PATH, description = "리뷰의 numeric Id", required=true, schema=@Schema()) @PathVariable("reviewId") Integer reviewId,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Object body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Review>(objectMapper.readValue("{\n  \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"reviewForm\" : {\n    \"id\" : 5\n  },\n  \"anonymous\" : {\n    \"isAnonymous\" : true,\n    \"anonymousName\" : \"anonymousName\"\n  },\n  \"id\" : 1\n}", Review.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Review>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Review>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> updateRoomsInBuildings(@Parameter(in = ParameterIn.PATH, description = "빌딩의 numeric ID", required=true, schema=@Schema()) @PathVariable("buildingId") Integer buildingId,@Parameter(in = ParameterIn.DEFAULT, description = "호실 정보를 수정하기 위한 roomsList", required=true, schema=@Schema()) @Valid @RequestBody List<Room> body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
