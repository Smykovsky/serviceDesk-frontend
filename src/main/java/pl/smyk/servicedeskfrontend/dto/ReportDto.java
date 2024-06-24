package pl.smyk.servicedeskfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ReportDto {
    private Long id;
    private String title;
    private String description;
    private String createdBy;
    private String assignedUser;
    private String priority;
    private String status;

    public ReportDto() {
    }

}
