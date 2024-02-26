package org.example.sandvillageupload;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "fileUploadExecutor")
    public ThreadPoolTaskExecutor fileUploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15); // 根据需要调整线程池的大小
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(300);
        executor.setThreadNamePrefix("FileUpload-");
        executor.initialize();
        return executor;
    }
}
