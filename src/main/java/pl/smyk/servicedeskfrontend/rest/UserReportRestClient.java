package pl.smyk.servicedeskfrontend.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserReportRestClient {
    private static final String REPORTS_URL = "http://localhost:8080/api/report";
    private final RestTemplate restTemplate;

    public UserReportRestClient() {
        this.restTemplate = new RestTemplate();
    }


    public ResponseEntity<?> createReport(ReportDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(dto, headers);

        ResponseEntity<ReportDto> response = restTemplate.exchange(
                REPORTS_URL,
                HttpMethod.POST,
                entity,
                ReportDto.class
        );

        return response;
    }

    public List<ReportDto> getMyNotAssignedReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ReportDto[]> response = restTemplate.exchange(
                REPORTS_URL + "/notAssigned",
                HttpMethod.GET,
                entity,
                ReportDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public List<ReportDto> getMyAssignedReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ReportDto[]> response = restTemplate.exchange(
                REPORTS_URL + "/assigned",
                HttpMethod.GET,
                entity,
                ReportDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public List<ReportDto> getMyClosedReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ReportDto[]> response = restTemplate.exchange(
                REPORTS_URL + "/closed",
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
}
