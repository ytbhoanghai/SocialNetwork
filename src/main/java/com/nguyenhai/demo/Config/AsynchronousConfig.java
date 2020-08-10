package com.nguyenhai.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsynchronousConfig {

    @Bean(name = "generateFileTaskExecutor")
    public TaskExecutor taskExecutorForGenerateFile() {
        return createThreadPoolTaskExecutor(5, 5, 600);
    }

    @Bean(name = "sendEmailExecutor")
    public TaskExecutor taskExecutorForSendEmail() {
        return createThreadPoolTaskExecutor(3,3, 300);
    }

    @Bean(name = "notificationExecutor")
    public TaskExecutor taskExecutorForNotification() {
        return createThreadPoolTaskExecutor(10, 10, 300);
    }

    @Bean(name = "createPostExecutor")
    public TaskExecutor taskExecutorForCreatePost() {
        return createThreadPoolTaskExecutor(10, 10, 600);
    }

    private ThreadPoolTaskExecutor createThreadPoolTaskExecutor(int corePoolSize, int maxPoolSize, int queueCapacity) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.afterPropertiesSet();
        return threadPoolTaskExecutor;
    }

}
