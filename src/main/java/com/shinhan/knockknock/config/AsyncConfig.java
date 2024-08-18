package com.shinhan.knockknock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 기본 스레드 수: 최초 2개 스레드로 처리 시작
        executor.setCorePoolSize(2);
        // Queue 사이즈: 처리가 밀리는 경우 100개 사이즈 Queue에서 대기하다가
        executor.setQueueCapacity(100);
        // 최대 스레드 수: 3. Queue 용량보다 많은 요청이 발생할 경우 10개 스레드까지 생성해서 처리
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }
}
