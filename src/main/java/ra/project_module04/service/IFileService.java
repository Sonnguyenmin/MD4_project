package ra.project_module04.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService{
    String uploadLocal(MultipartFile fileUpload);
    String uploadFirebase(String localPath);
}
