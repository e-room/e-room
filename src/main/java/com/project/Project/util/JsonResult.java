package com.project.Project.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult {

    private HttpStatus code;

    private String message;

    private Object data;

    @Override
    public String toString() {
        return "JSONResult [code = " + code + ", message= " + message + ", data= " + data + "]";
    }
}
