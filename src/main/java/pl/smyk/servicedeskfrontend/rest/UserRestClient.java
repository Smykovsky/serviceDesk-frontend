package pl.smyk.servicedeskfrontend.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.util.Arrays;
import java.util.List;

public class UserRestClient {
    private static final String REPORTS_URL = "http://localhost:8080/api/report";
    private final RestTemplate restTemplate;

    public UserRestClient() {
        restTemplate = new RestTemplate();
    }


    public List<ReportDto> getAllMyReportsNotAssigned() {
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

    public List<ReportDto> getAllMyReportsAssigned() {
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

    public List<ReportDto> getAllMyReportsClosed() {
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

//    public ReportDto createReport() {
//
//    }
//
//    public ReportDto updateReport() {
//
//    }

}
