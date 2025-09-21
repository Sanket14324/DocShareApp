package docshare.com.service.controller;

import docshare.com.service.services.impl.AmazonClient;
import com.amazonaws.services.s3.model.S3Object;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;

@Controller
public class FileController {
//
    @Autowired
    private AmazonClient service;

    @GetMapping("/download/{id}")
    @ResponseBody
    public HttpEntity<byte[]> downloadFile(@PathVariable("id") Long fileId, HttpServletResponse response) {
        S3Object data = service.downloadFile(fileId);

        String s3ObjectKey = data.getKey();
        String fileName = s3ObjectKey.substring(s3ObjectKey.lastIndexOf('/') + 1).substring(14);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(data.getObjectMetadata().getContentType()));
        header.setContentLength(data.getObjectMetadata().getContentLength());
        header.setContentDispositionFormData("attachment", fileName);
        byte[] inputStream = null;
        try{
            inputStream = data.getObjectContent().readAllBytes();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new HttpEntity<byte[]>(inputStream, header);
    }

    @DeleteMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName, Authentication authentication) {
        if(authentication == null){
            return "redirect:/login";
        }
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                service.deleteFile(fileName);
                return "/admin/comments";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
                service.deleteFile(fileName);
                return "/user/comments";
            }
        }
        return "redirect:/";
    }
}
