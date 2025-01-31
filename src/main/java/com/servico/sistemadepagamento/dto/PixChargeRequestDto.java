package com.servico.sistemadepagamento.dto;

public record PixChargeRequestDto(
        String debtorName,
        String cpf,
        String value,
        String informationName1,
        String informationContent1,
        String informationName2,
        String informationContent2) {
}
