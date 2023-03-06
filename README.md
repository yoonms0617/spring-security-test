## 스프링 시큐리티를 테스트 하는 방법

### 프로젝트 환경

- Java 11
- Spring Boot v2.7.9

```text
# 의존성 목록
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-validation'
implementation 'org.springframework.boot:spring-boot-starter-web'

annotationProcessor 'org.projectlombok:lombok'
compileOnly 'org.projectlombok:lombok'
    
runtimeOnly 'com.h2database:h2'

testImplementation 'org.springframework.boot:spring-boot-starter-test'
testImplementation 'org.springframework.security:spring-security-test'
```