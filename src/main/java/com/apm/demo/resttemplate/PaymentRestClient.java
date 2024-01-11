package com.apm.demo.resttemplate;

import com.apm.demo.models.Booking;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("WeakerAccess")
public class PaymentRestClient {

    private RestTemplate restTemplate;

    public PaymentRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void postForEntity(String paymentServiceUrl, Booking booking) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("booking_id", booking.getId());
        jsonObject.put("type", "card");
        HttpEntity<JSONObject> request = new HttpEntity<>(jsonObject, headers);
        String result = restTemplate.postForObject(paymentServiceUrl, request, String.class);
    }
}
