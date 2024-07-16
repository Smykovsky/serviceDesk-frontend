package pl.smyk.servicedeskfrontend.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private Long articleId;
    private String comment;
}
