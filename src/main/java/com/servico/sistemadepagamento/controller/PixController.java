package com.servico.sistemadepagamento.controller;

import com.servico.sistemadepagamento.dto.PixChargeRequestDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.servico.sistemadepagamento.service.PixService;

@RestController
@RequestMapping(value = "/api/pix", produces = MediaType.APPLICATION_JSON_VALUE)
public class PixController {

    private final PixService pixService;

    @Autowired
    public PixController(PixService pixService) {
        this.pixService = pixService;
    }

    @GetMapping
    public ResponseEntity<String> pixCreate() {
        JSONObject response = pixService.pixCreate();
        return ResponseEntity.ok()
                .body(response.toString());
    }

    @GetMapping("/v1/pixList")
    public ResponseEntity<String> pixList() {
        JSONObject response = pixService.pixList();
        return ResponseEntity.ok()
                .body(response.toString());
    }

    @GetMapping("/v1/pixReceived")
    public ResponseEntity<String> pixReceived(@RequestParam("start") String start, @RequestParam("end") String end) {
        JSONObject response = pixService.pixReceived(start, end);
        return ResponseEntity.ok()
                .body(response.toString());
    }

    @PostMapping("/v1/pixCreateCharge")
    public ResponseEntity<String> pixCreateCharge(@RequestBody PixChargeRequestDto pixChargeRequestDto)  {
        JSONObject response = this.pixService.pixCreateCharge(pixChargeRequestDto);
        return ResponseEntity.ok()
                .body(response.toString());
    }

    @GetMapping("/v1/pixCharge")
    public ResponseEntity<String> pixDetailsCharge(@RequestParam("txId") String txId) {
        var response = pixService.pixDetailsCharge(txId);
        return ResponseEntity.ok()
                .body(response.toString());
    }

    @DeleteMapping
    public ResponseEntity<String> pixDelete(@RequestParam("pixKey") String pixKey) {
        var response = pixService.pixDelete(pixKey);
        return ResponseEntity.ok()
                .body(response.toString());
    }
}