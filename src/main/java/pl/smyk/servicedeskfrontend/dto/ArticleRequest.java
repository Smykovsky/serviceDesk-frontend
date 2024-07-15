package pl.smyk.servicedeskfrontend.dto;

import lombok.Data;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArticleRequest {
    private String title;
    private String description;
    private FileSystemResource file;
}
