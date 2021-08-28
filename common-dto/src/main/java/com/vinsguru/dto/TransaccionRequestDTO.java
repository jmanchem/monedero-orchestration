package com.vinsguru.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TransaccionRequestDTO {

    private String phoneOrigen;
    private String phoneDestino;
    private Double amount;
    private UUID TransaccionId;

}