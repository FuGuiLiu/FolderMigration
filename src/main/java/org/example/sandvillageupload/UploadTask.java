package org.example.sandvillageupload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class UploadTask implements Callable<Boolean> {
	private final UploadVillage uploadVillage;
	private final int maxRetries;
	private final String villageUploadDestinationPath;
	private int attempts = 0;

	public UploadTask(UploadVillage uploadVillage, int maxRetries, String villageUploadDestinationPath) {
		this.uploadVillage = uploadVillage;
		this.maxRetries = maxRetries;
		this.villageUploadDestinationPath = villageUploadDestinationPath;
	}

	@Override
	public Boolean call() throws Exception {
		while (attempts < maxRetries) {
			try {
				// 假设这是上传文件的方法，这里需要实现实际的上传逻辑
				uploadFileChunk(uploadVillage);
				return true; // 上传成功
			} catch (Exception e) {
				attempts++;
				System.out.println(Thread.currentThread().getName() + " Upload failed, attempt " + attempts + ". Retrying...");
				Thread.sleep(1000);
				// 可以在这里加入一些延时逻辑，例如 Thread.sleep(1000);
			}
		}
		return false; // 所有重试尝试都失败
	}

	private void uploadFileChunk(UploadVillage uploadVillage) throws Exception {
		MultipartFile multipartFile = uploadVillage.getMultipartFile();
		// 根据文件的原始名称和目标路径创建目录和文件路径
		String fileName = multipartFile.getOriginalFilename();
		Path directoryPath = Paths.get(villageUploadDestinationPath + File.separator + fileName.replace(uploadVillage.getName(), ""));
		// 检查目录是否存在，如果不存在则创建
		if (!Files.exists(directoryPath)) {
			Files.createDirectories(directoryPath);
		} else {
			System.out.println("目录 " + directoryPath + " 已存在，跳过创建。");
		}
		Path filePath = directoryPath.resolve(uploadVillage.getName());
		System.out.println(Thread.currentThread().getName() + "当前文件 " + multipartFile.getOriginalFilename() + "大小为" + multipartFile.getSize());

		// 保存文件到目标目录
		// 检查文件是否已经存在，如果不存在则保存文件
		if (!Files.exists(filePath)) {
			multipartFile.transferTo(filePath.toFile());
			System.out.println(Thread.currentThread().getName() + "文件上传成功: " + filePath);
		} else {
			System.out.println("文件 " + fileName + " 已存在，跳过创建。");
		}
	}
}
