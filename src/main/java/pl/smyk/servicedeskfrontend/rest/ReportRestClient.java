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

public class ReportRestClient {
    private static final String REPORTS_URL = "http://localhost:8080/api/report";
    private final RestTemplate restTemplate;

    public ReportRestClient() {
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

}
