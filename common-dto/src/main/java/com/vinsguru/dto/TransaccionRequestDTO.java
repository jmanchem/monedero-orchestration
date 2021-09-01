package com.vinsguru.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TransaccionRequestDTO {

    private UUID TransaccionId;
    private String phoneOrigen;
    private String phoneDestino;
    private Double amount;

}