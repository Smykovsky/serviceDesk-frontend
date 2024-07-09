package pl.smyk.servicedeskfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String description;
    private String createdBy;
    private String updatedBy;
    private String attachmentPath;
    private List<String> comments;
}
