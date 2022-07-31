package com.project.Project.Util;

import lombok.*;
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
    public String toString(){
        return "JSONResult [code = " + code + ", message= " + message + ", data= " + data + "]";
    }
}
