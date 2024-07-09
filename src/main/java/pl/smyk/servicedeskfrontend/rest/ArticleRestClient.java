package pl.smyk.servicedeskfrontend.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.smyk.servicedeskfrontend.dto.ArticleDto;
import pl.smyk.servicedeskfrontend.dto.ReportDto;
import pl.smyk.servicedeskfrontend.session.SessionManager;

import java.util.Arrays;
import java.util.List;

public class ArticleRestClient {
    private static final String REPORTS_URL = "http://localhost:8080/api/article";
    private final RestTemplate restTemplate;

    public ArticleRestClient() {
        this.restTemplate = new RestTemplate();
    }


    public List<ArticleDto> getAllArticles() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SessionManager.getInstance().getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ArticleDto[]> response = restTemplate.exchange(
                REPORTS_URL,
                HttpMethod.GET,
                entity,
                ArticleDto[].class
        );

        return Arrays.asList(response.getBody());
    }

}
