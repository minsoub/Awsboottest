### IntelliJ 커뮤니티 버전으로 스프링 부트 개발 시 추가사항
- IntelliJ 커뮤니티 버전으로 Gradle Project로 생성한 후 build.gradle 파일을 열어 보면 아래와 같다.
```groovy
plugins {
    id 'java'
}

group 'com.bithumb'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
}

test {
    useJUnitPlatform()
}
```
- 위의 build.gradle 파일을 스프링 부트 프로젝트로 변경하기 위해서 아래와 같이 수정한다.
```groovy
plugins {
    id 'org.springframework.boot' version '2.6.3'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'
    id 'war'
}

group 'com.bithumb'
version '1.0-SNAPSHOT'
sourceCompatibility = '11'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}
repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'junit:junit:4.13.1'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    annotationProcessor 'org.projectlombok:lombok'
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}
```
### Project에 Spring Data JPA 적용 
- build.gradle에 의존성을 추가한다.
- h2
  인메모리 관계형 데이터베이스   
  별도의 설치가 필요 없이 프로젝트 의존성만은로 관리   
  메모리에서 실행되어 어플리케이션 재시작 시 초기화   
  로컬 환경에서 테스트 용도로 사용   
```groovy
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
  implementation 'com.h2database:h2'
}
```

### lombok Enable
- Ctrl+Shift + A 버튼을 클릭 후 Plugin 으로 검색
- lombok plugin을 선택 후 설치한다.
- 설치 후 File > Settings 메뉴 클릭
  Build > Compiler > Annotation Processor 를 선택 한 후    
  Enable annotation processing을 체크
### JUnit 테스트 
- build.gradle 파일에 아래 내용을 추가한다.
```groovy
dependencies {
    ...
    implementation 'junit:junit:4.13.1'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```
- HomeController 클래스 작성 
```java
package com.bithumb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "Home";
    }
}

```
- 테스트 클랙스 작성
```java
package com.bithumb.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HomeController.class)
public class HomeControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void Home_test() throws Exception {
        String hello = "Home";

        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }
}
```
### JUnit Test - Parameter 
- Paramter를 사용하는 테스트 메소드 정의
```java
    @Test
    public void Home_dto_test() throws Exception {
        String name = "Hello";
        int amount = 10000;

        mvc.perform(
                get("/home/name")
                        .param("name", name)
                        .param("amount", String.valueOf(amount))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))  // jsonPath => $를 기준으로 필드명을 명시한다.
                .andExpect(jsonPath("$.amount", is(amount)));

    }
```
- HomeController에서 정의된 메소드
```java
    @GetMapping("/home/name")
    public HomeResponseDto getName(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return new HomeResponseDto(name, amount);
    }
```

