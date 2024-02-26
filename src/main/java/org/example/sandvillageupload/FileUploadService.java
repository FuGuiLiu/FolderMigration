package org.example.sandvillageupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class FileUploadService {

	// 异步方法，用于上传文件
	@Autowired
	@Qualifier(value = "fileUploadExecutor")
	private ThreadPoolTaskExecutor fileUploadExecutor;

	@Value("${villageUploadDestinationPath}")
	private String villageUploadDestinationPath;

	public void uploadFiles(List<UploadVillage> uploadVillages, int maxRetries) throws IOException, InterruptedException {
		List<Future<Boolean>> futures = new ArrayList<>();
		for (UploadVillage uploadVillage : uploadVillages) {
			UploadTask task = new UploadTask(uploadVillage, maxRetries, villageUploadDestinationPath);
			futures.add(fileUploadExecutor.submit(task));
		}

		// 等待所有上传任务完成
		for (Future<Boolean> future : futures) {
			try {
				boolean success = future.get();
				System.out.println("Upload result: " + success);
			} catch (Exception e) {
				System.out.println("Error during file upload.");
			}
		}

		// fileUploadExecutor.shutdown();
	}
}