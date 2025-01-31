package com.servico.sistemadepagamento.controller;

import com.servico.sistemadepagamento.dto.WebhookConfig;
import com.servico.sistemadepagamento.service.WebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/webhook")
@Slf4j
public class WebhookController {

    private final WebhookService webhookService;
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public WebhookController(WebhookService webhookService, KafkaTemplate<String, String> kafkaTemplate) {
        this.webhookService = webhookService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/pix")
    public ResponseEntity<String> webhookPix(@RequestBody String body) {
        log.info("Recebendo um Post no /webhook/pix body {}", body);
        kafkaTemplate.send("payment-confirmation", body);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/registerWebhook")
    public ResponseEntity<String> webhook(@RequestParam("pixKey") String pixKey, @RequestBody WebhookConfig body) {
        log.info("Recebendo um Post no /webhook body {}", body);
        webhookService.paymentConfirm(pixKey, body.webhookUrl());
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
