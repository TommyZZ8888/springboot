# Springboot3集成Swagger3.0

> 本着什么都用最新的的原则，今天在这个项目里集成Swagger2 3.0版，然后就开始各种踩坑

开始的时候引入了这两个依赖
```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>3.0.0</version>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>3.0.0</version>
    <scope>compile</scope>
</dependency>
```
跟着一个低版本的教程写了一个配置类，并在入口类上添加`@EnableSwagger2`
```Java
//@Configuration
public class Swagger2 {

    /**
     * 配置swagger2 docker
     */
    @Bean
    public Docket creatRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // 制定api类型为swagger2
                .apiInfo(apiInfo())  // 指定api文档信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cevier.shop.controller"))  // 指定接口所在包
                .paths(PathSelectors.any())  // 指定需要选择的controller
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CvrShop接口API")
                .contact(new Contact("cevier", "https://cevier.com", "mail@cevier.com"))
                .description("电商项目学习")
                .version("0.0.1")
                .termsOfServiceUrl("https://shop.cevier.com")
                .build();
    }
}

@EnableSwagger2
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
直接运行报错`java.lang.TypeNotPresentException: Type javax.servlet.http.HttpServletRequest not present`
查了一下也没太明白这是什么原因，好像跟Spingboot3有关系，按照[stack overflow](https://stackoverflow.com/questions/71549614/springfox-type-javax-servlet-http-httpservletrequest-not-present)上面的方法引入了一个新依赖，并添加了一行配置
```xml
<!-- https://mvnrepository.com/artifact/javax.servlet/servlet-api -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>servlet-api</artifactId>
    <version>2.5</version>
    <scope>provided</scope>
</dependency>
```
```yml
spring:
  mvc:
  pathmatch:
    matching-strategy: ANT_PATH_MATCHER
```
到这里程序能启动了，但[swagger-ui](http://localhost:8080/swagger-ui/index.html)访问不了
然后又去网上找解决办法，发现原来swagger3有一个springboot starter把它放到项目里继续尝试
```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```
```java

@Configuration
public class Swagger3 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                // 定义是否开启swagger，false为关闭，可以通过变量控制
                .enable(true)
                // 将api的元信息设置为包含在json ResourceListing响应中。
                .apiInfo(apiInfo())
                // 选择哪些接口作为swagger的doc发布
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cevier.shop.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(getGlobalRequestParameters())
                .globalResponses(HttpMethod.GET, getGlobalResponseMessage())
                .globalResponses(HttpMethod.POST, getGlobalResponseMessage());
    }

    /**
     * API 页面上半部分展示信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CvrShop接口API")
                .contact(new Contact("cevier", "https://cevier.com", "mail@cevier.com"))
                .description("电商项目学习")
                .version("0.0.1")
                .termsOfServiceUrl("https://shop.cevier.com")
                .build();
    }

    /**
     * 封装全局通用参数
     */
    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("uuid")
                .description("设备uuid")
                .required(true)
                .in(ParameterType.QUERY)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                .build());
        return parameters;
    }


    /**
     * 封装通用响应信息
     */
    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("未找到资源").build());
        return responseList;
    }

}
```
运行程序，报错`java.lang.TypeNotPresentException: Type javax.servlet.http.HttpServletRequest not present`
按之前的解决方法，引入依赖，加配置解决这个问题
这时候程序能启动，但依旧访问不了[swagger-ui](http://localhost:8080/swagger-ui/index.html)
继续在网上看资料，注意到好像没加
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```
加上springdoc-openapi后启动程序，访问[swagger-ui](http://localhost:8080/swagger-ui/index.html)又报错了`java.lang.NoSuchMethodError: 'boolean io.swagger.v3.oas.models.media.Schema.getExampleSetFlag()'`
也不是很清楚什么原因，看了一下别人的代码，在主类上加`@EnableOpenApi`
这时候换了个错误`java.lang.IllegalStateException: Ambiguous handler methods mapped for '/v3/api-docs'`
继续上网找原因，[大概是springfox-boot-starter和springdoc-openapi-starter-webmvc-ui同时能够处理/v3/api-docs的请求](https://stackoverflow.com/questions/69171098/got-ambiguous-handler-methods-mapped-for-v3-api-docs-after-migrating-from-s)<br/>
到这里开始放弃springfox-boot-starter，自己手动一个一个导入依赖，并且没在主类上添加swagger相关的注释
```xml
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>servlet-api</artifactId>
    <version>2.5</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>3.0.0</version>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>3.0.0</version>
    <scope>compile</scope>
</dependency>
```
终于能访问到[swagger-ui](http://localhost:8080/swagger-ui/index.html)了<br/>
后面想要好看一点，开始集成swagger-bootstrap-ui，引入依赖后发现浏览器上看不到内容
```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>swagger-bootstrap-ui</artifactId>
    <version>1.9.6</version>
</dependency>
```
查了一下才知道swagger-bootstrap-ui是knife4j的前生，于是UI把依赖换成了knife4j
```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.0.0</version>
</dependency>
```
```java

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CvrShop接口API")
                        .version("1.0")
                        .description("电商项目学习")
                        .termsOfService("https://shop.cevier.com")
                        .contact(new Contact().name("cevier").url("https://cevier.com").email("mail@cevier.com"))
                );
    }
}

```
才发现，原来只要上面十几行代码就可以集成swagger3，到这花了一晚上时间，真心累


---
By: Cevier 2023.01.15