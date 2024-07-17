package pl.smyk.servicedeskfrontend.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.smyk.servicedeskfrontend.dto.*;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OperatorRestClient {
    private static final String REPORTS_URL = "http://localhost:8080/api/report";
    private final RestTemplate restTemplate;

    public OperatorRestClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<ReportDto> getAllNotAssignedReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ReportDto[]> response = restTemplate.exchange(
                REPORTS_URL + "/all/reports-notAssigned",
                HttpMethod.GET,
                entity,
                ReportDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public List<ReportDto> getMyNotClosedReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ReportDto[]> response = restTemplate.exchange(
                REPORTS_URL + "/user/reports-assigned",
                HttpMethod.GET,
                entity,
                ReportDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public List<ReportDto> getMyAllClosedReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ReportDto[]> response = restTemplate.exchange(
                REPORTS_URL + "/user/reports-closed",
                HttpMethod.GET,
                entity,
                ReportDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public List<ReportDto> getAllAssignedReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ReportDto[]> response = restTemplate.exchange(
                REPORTS_URL + "/all/reports-assigned",
                HttpMethod.GET,
                entity,
                ReportDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public List<ReportDto> getMyInProgressReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ReportDto[]> response = restTemplate.exchange(
                REPORTS_URL + "/user/reports-inProgress",
                HttpMethod.GET,
                entity,
                ReportDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public List<ReportDto> getAllSolvedReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ReportDto[]> response = restTemplate.exchange(
                REPORTS_URL + "/all/reports-closed",
                HttpMethod.GET,
                entity,
                ReportDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public HashMap<String, Integer> getDashboardData() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<HashMap<String, Integer>> response = restTemplate.exchange(
                "http://localhost:8080/api/dashboard/data",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<HashMap<String, Integer>>() {}
        );

        return response.getBody();
    }

    public List<String> loadOperatorUsernames() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String[]> response = restTemplate.exchange(
                "http://localhost:8080/api/operator",
                HttpMethod.GET,
                entity,
                String[].class
        );

        return Arrays.asList(response.getBody());
    }

    public ResponseEntity<?> assignReportToOperator(AssignReportRequest dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(dto, headers);

        ResponseEntity<?> response = restTemplate.exchange(
                REPORTS_URL + "/assign",
                HttpMethod.POST,
                entity,
                ResponseEntity.class
        );
        return response;
    }

    public ResponseEntity<?> assignReportLoggedOperator(Long reportId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(reportId, headers);

        ResponseEntity<?> response = restTemplate.exchange(
                REPORTS_URL + "/assign-logged",
                HttpMethod.POST,
                entity,
                ResponseEntity.class
        );
        return response;
    }

    public ResponseEntity<?> closeReport(Long reportId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(reportId, headers);

        ResponseEntity<?> response = restTemplate.exchange(
                REPORTS_URL + "/close",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }

    public ResponseEntity<?> changeReportStatusToInProgress(Long reportId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(reportId, headers);

        ResponseEntity<?> response = restTemplate.exchange(
                REPORTS_URL + "/inProgress",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }

    public ResponseEntity<?> addArticle(ArticleRequest dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", dto.getTitle());
        body.add("description", dto.getDescription());

        if (dto.getFile() != null) {
            body.add("file", dto.getFile());
        }

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<ArticleDto> response = restTemplate.exchange(
                "http://localhost:8080/api/article",
                HttpMethod.POST,
                entity,
                ArticleDto.class
        );

        return response;
    }

    public ResponseEntity<?> updateArticle(ArticleDto articleDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(articleDto, headers);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:8080/api/article",
                HttpMethod.PUT,
                entity,
                String.class
        );

        return response;
    }
}
