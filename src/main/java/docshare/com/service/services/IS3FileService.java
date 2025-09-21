package docshare.com.service.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IS3FileService {

    String deleteFile(String fileName);
    byte[] downloadFile(String fileName);

    String uploadFile(MultipartFile file) throws IOException;

}
