package com.servico.sistemadepagamento.controller;

import com.servico.sistemadepagamento.dto.WebhookConfig;
import com.servico.sistemadepagamento.service.WebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/api/")
@Slf4j
public class WebhookController {

    private final WebhookService webhookService;

    @Autowired
    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @GetMapping
    public String hello() {
        log.info("Recebendo GET Hello /");
        return "Webhook Hello";
    }

    @PostMapping("/webhook/pix")
    public ResponseEntity<String> webhookPix(@RequestBody String body) {
        log.info("Recebendo um Post no /webhook/pix body {}", body);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestParam("pixKey") String pixKey, @RequestBody WebhookConfig body) {
        log.info("Recebendo um Post no /webhook body {}", body);
        webhookService.confirmPayment(pixKey, body.webhookUrl());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/webhookList")
    public ResponseEntity<String> webhooksList(@RequestParam("start") String start, @RequestParam("end") String end) {
        var response = webhookService.registeredWebhooks(start, end);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

    @DeleteMapping
    public ResponseEntity<String> webhookDelete(@RequestParam("pixKey") String pixKey) {
        var response = webhookService.pixDeleteWebhook(pixKey);
        return ResponseEntity.ok()
                .body(response.toString());
    }
}
