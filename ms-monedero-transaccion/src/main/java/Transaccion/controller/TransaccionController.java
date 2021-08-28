package Transaccion.controller;

import com.vinsguru.dto.OrderRequestDTO;
import com.vinsguru.dto.OrderResponseDTO;
import Transaccion.entity.Transaccion;
import Transaccion.service.TransaccionService;
import com.vinsguru.dto.TransaccionRequestDTO;
import com.vinsguru.dto.TransaccionrResponseDTO;
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

    @GetMapping("/all")
    public List<TransaccionrResponseDTO> getOrders(){
        return this.service.getAll();
    }

}
