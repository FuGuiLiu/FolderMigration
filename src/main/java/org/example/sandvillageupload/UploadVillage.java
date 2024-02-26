package org.example.sandvillageupload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @packageName:org.example.sandvillageupload
 * @ClassName:UploadVillage
 * @Author:l
 * @Data:2024-02-23 14:41:30
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadVillage {
	private MultipartFile multipartFile;
	private long lastModified;
	private String lastModifiedDate;
	private String name;
	private long size;
	private String type;
	private String webkitRelativePath;
}
