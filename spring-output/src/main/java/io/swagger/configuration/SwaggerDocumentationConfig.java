package io.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-05T06:53:15.751Z[GMT]")
@Configuration
public class SwaggerDocumentationConfig {

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.OAS_30)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("io.swagger.api"))
                    .build()
                .directModelSubstitute(org.threeten.bp.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.threeten.bp.OffsetDateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("이룸 Project")
            .description("Project를 위한 API 명세서 작성 Entity에 따라 충분히 변경 가능합니다.  해당 문서의 사용법에 대해 설명하자면 - openapi: 버전 - info: api 정보를 나타낼 수 있습니다. - servers: 명시한 api의 대상이 되는 서버를 배열로 나타냅니다. - Components: api에서 공통으로 사용되는 모델을 선언합니다.  모델 자체를 정의하는 schemas와 api request/response를 정의하는 parameters, responses 등의 하위 옵션이 있습니다. - paths: 제공되는 api를 나열합니다. ")
            .license("Not yet")
            .licenseUrl("???")
            .termsOfServiceUrl("")
            .version("1.0.0")
            .contact(new Contact("","", "koallarry11@kaist.ac.kr"))
            .build();
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .info(new Info()
                .title("이룸 Project")
                .description("Project를 위한 API 명세서 작성 Entity에 따라 충분히 변경 가능합니다.  해당 문서의 사용법에 대해 설명하자면 - openapi: 버전 - info: api 정보를 나타낼 수 있습니다. - servers: 명시한 api의 대상이 되는 서버를 배열로 나타냅니다. - Components: api에서 공통으로 사용되는 모델을 선언합니다.  모델 자체를 정의하는 schemas와 api request/response를 정의하는 parameters, responses 등의 하위 옵션이 있습니다. - paths: 제공되는 api를 나열합니다. ")
                .termsOfService("")
                .version("1.0.0")
                .license(new License()
                    .name("Not yet")
                    .url("???"))
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .email("koallarry11@kaist.ac.kr")));
    }

}
