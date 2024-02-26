package org.example.sandvillageupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileUploadController {

	private final FileUploadService fileUploadService;


	@Autowired
	public FileUploadController(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}

	@GetMapping("/")
	public String index() {
		return "upload"; // 返回上传表单的HTML页面名
	}

	@PostMapping("/uploadFiles")
	@ResponseBody
	public String uploadFolder(@RequestParam("file") MultipartFile[] file,
	                           @RequestParam("lastModified") long[] lastModified,
	                           @RequestParam("lastModifiedDate") String[] lastModifiedDate,
	                           @RequestParam("name") String[] name,
	                           @RequestParam("size") long[] size,
	                           @RequestParam("type") String[] type,
	                           @RequestParam("webkitRelativePath") String[] webkitRelativePath) throws IOException, InterruptedException {
		List<UploadVillage> uploadVillages = new ArrayList<>();
		for (int i = 0; i < file.length; i++) {
			UploadVillage village = new UploadVillage(file[i], lastModified[i], lastModifiedDate[i],
					name[i], size[i], type[i], webkitRelativePath[i]);
			uploadVillages.add(village);
		}
		fileUploadService.uploadFiles(uploadVillages, 3);
		return "files upload successfully";
	}
}