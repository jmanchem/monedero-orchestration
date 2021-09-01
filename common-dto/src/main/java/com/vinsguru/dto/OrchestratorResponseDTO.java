package com.vinsguru.dto;

import com.vinsguru.enums.OrderStatus;
import com.vinsguru.enums.TransaccionesStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class OrchestratorResponseDTO {

    private Integer userId;
    private Integer productId;
    private UUID orderId;
    private Double amount;
    private OrderStatus status;
    private TransaccionesStatus statusT;
    private UUID TransaccionId;
}
