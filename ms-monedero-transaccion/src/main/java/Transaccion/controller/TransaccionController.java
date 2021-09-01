package Transaccion.controller;

import com.vinsguru.dto.*;
import Transaccion.entity.Transaccion;
import Transaccion.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("transaction")
public class TransaccionController {

    @Autowired
    private TransaccionService service;

    @PostMapping("/transfer")
    public Transaccion createTransfer(@RequestBody TransaccionRequestDTO requestDTO){
        requestDTO.setTransaccionId(UUID.randomUUID());
        return this.service.createTransfer(requestDTO);
    }
    @PostMapping("/debit") // Poner deuda
    public PaymentResponseDTO debit(@RequestBody PaymentRequestDTO requestDTO){
        return this.service.debit(requestDTO);
    }

    @PostMapping("/credit")// Poner pago
    public void credit(@RequestBody PaymentRequestDTO requestDTO){
        this.service.credit(requestDTO);
    }

    @GetMapping("/all")
    public List<TransaccionrResponseDTO> getOrders(){
        return this.service.getAll();
    }

}
