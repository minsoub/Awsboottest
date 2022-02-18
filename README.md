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

### Spring Layer 구성 
- Web Layer
  - 컨트롤러(@Controller)와 JSP/Freemaker 등의 뷰 템플릿 영역
  - 필터(@Filter),  인터셉터, 컨트롤러 어드바이스(@ControllerAdvice) 등 외부 요청과 응답에 대한 잔반적인 영역
- Service Layer
  - @Service에 사용되는 서비스 영역
  - Controller와 DAO의 중간 영역에서 사용
  - @Transaction이 사용되어야 하는 영역
- Repository Layer
  - Database의 데이터 저장소에 접근하는 영역
  - 이전의 Data Access Object 영역
- DTOs
  - Data Transfer Object는 계층 간에 데이터 교환을 위한 객체로 해당 영역을 말함 
  - 뷰 템플릿 엔진에서 사용될 객체나 Repository Layer에서 결과로 넘겨준 객체 등 
- Domain Model
  - @Entity가 사용되는 영역이자만 I/O처럼 값 객체들오 이 영역에 해당 될 수 있음.
  - 비즈니스 로직 처리를 여기서 수행.
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
### JUnit Test - JPA Test
- @WebMvcTest를 사용하지 않고 TestRestTemplate을 사용.
- @WebMvcTest의 경우 JPA 기능이 작동하지 않음
- @WebMvcTest의 경우 Controller와 ControllerAdvice 등 외부 연동과 관련된 부분만 활성화
- JPA 기능까지 한번에 테스트할 때는 @SpringBootTest와 TestRestTemplate을 사용함
```java
package com.bithumb.controller;

import com.bithumb.domain.board.Boards;
import com.bithumb.domain.board.BoardsRepository;
import com.bithumb.dto.BoardsSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BoardsRepository boardsRepository;

    @After
    public void tearDown() throws Exception {
        boardsRepository.deleteAll();
    }

    @Test
    public void board_resgister() throws Exception {
        String title = "board title test";
        String content = "board content test";

        BoardsSaveRequestDto requestDto = BoardsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("minsoub@gmail.com")
                .build();
        String url = "http://localhost:"+port+"/api/v1/boards";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Boards> all = boardsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }
}

```
