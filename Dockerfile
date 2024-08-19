# 첫 번째 단계: 빌드 단계
FROM gradle:7.5.1-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 캐시를 활용하기 위해 build.gradle과 settings.gradle을 복사
COPY build.gradle settings.gradle /app/

# 의존성 다운받기
RUN gradle build -x test --no-daemon

# 모든 소스코드 복사
COPY . /app/

# 프로젝트 빌드
RUN gradle build -x test --no-daemon

# 두 번째 단계: 실행 단계
FROM openjdk:17-jdk-slim

# 빌드 단계에서 생성된 jar 파일을 복사
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
