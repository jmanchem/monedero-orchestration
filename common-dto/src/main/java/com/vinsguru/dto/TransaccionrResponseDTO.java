package com.vinsguru.dto;

import com.vinsguru.enums.OrderStatus;
import com.vinsguru.enums.TransaccionesStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class TransaccionrResponseDTO {

    private UUID orderId;
    private String phoneOrigen;
    private String phoneDestino;
    private Double amount;
    private TransaccionesStatus status;

}
