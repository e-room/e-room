package com.project.Project;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomOAuth2AccountSecurityContextFactory.class)
public @interface WithMockCustomOAuth2Account {

    String name() default "name";

    String email() default "my@default.email";

    String picture() default "https://get_my_picture.com";

    String role() default "ROLE_USER";

    String restrationId() default "testAuthentication";

}