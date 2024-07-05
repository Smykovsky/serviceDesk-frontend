package pl.smyk.servicedeskfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignReportRequest {
    public Long reportId;
    public String username;
}
