package docshare.com.service.services.impl;

import docshare.com.service.constant.Constant;
import docshare.com.service.exception.AwsException;
import docshare.com.service.model.TicketDetailFile;
import docshare.com.service.repository.TicketDetailFileRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class AmazonClient {

    @Autowired
    private TicketDetailFileRepository ticketDetailFileRepository;
    @Autowired
    private AmazonS3 s3Client;

    @Value("${endpointUrl}")
    private String endpointUrl;
    @Value("${bucketName}")
    private String bucketName;
    public String uploadFile(MultipartFile multipartFile) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
//            s3Client.putObject().
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
            return fileUrl;
        } catch (Exception e) {
            throw new AwsException(e.getMessage());
        }
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3Client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
        return "Successfully deleted";
    }
    public S3Object downloadFile(Long id) {

        Optional<TicketDetailFile> ticketFile = ticketDetailFileRepository.findById(id);
        if(ticketFile.isPresent()) {
            String path  = ticketFile.get().getLocation();
            String pathSplit[] = path.split("/");
            String fileName = pathSplit[pathSplit.length - 1];
            try {
            S3Object s3Object = s3Client.getObject(bucketName, fileName);
            return s3Object;
            } catch (Exception e) {
                throw new AwsException(e.getMessage());
            }
        }
        return null;
    }


    public String deleteFile(String fileName) {
        try{
            s3Client.deleteObject(bucketName, fileName);
            return Constant.SUCCESS;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return Constant.FAIL;

    }

    private File convertMultiPartToFile(MultipartFile file) {

        File convFile = new File(file.getOriginalFilename());
        try{
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        PutObjectResult result = s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
    }

}
