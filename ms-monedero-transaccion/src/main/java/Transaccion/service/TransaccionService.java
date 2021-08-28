package Transaccion.service;

import com.vinsguru.dto.*;
import com.vinsguru.enums.OrderStatus;
import Transaccion.entity.Transaccion;
import Transaccion.repository.TransaccionRepository;
import com.vinsguru.enums.TransaccionesStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    // product price map
    /*private static final Map<Integer, Double> PRODUCT_PRICE =  Map.of(
            1, 100d,
            2, 200d,
            3, 300d
    );*/

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private FluxSink<OrchestratorRequestDTO> sink;

    public Transaccion createTransfer(TransaccionRequestDTO transaccionRequestDTO){
        Transaccion transaccion = this.transaccionRepository.save(this.dtoToEntity(transaccionRequestDTO));
        //this.sink.next(this.getOrchestratorRequestDTO(orderRequestDTO));
        return transaccion;
    }

    public List<TransaccionrResponseDTO> getAll() {
        return this.transaccionRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private Transaccion dtoToEntity(final TransaccionRequestDTO dto){
        Transaccion transaccion = new Transaccion();
        transaccion.setId(dto.getTransaccionId());
        transaccion.setPhoneOrigen(dto.getPhoneOrigen());
        transaccion.setPhoneDestino(dto.getPhoneDestino());
        transaccion.setAmount(dto.getAmount());
        transaccion.setStatus(TransaccionesStatus.TRANSACCION_CREATED);
        return transaccion;
    }

    private TransaccionrResponseDTO entityToDto(final Transaccion transaccion){
        TransaccionrResponseDTO dto = new TransaccionrResponseDTO();
        dto.setOrderId(transaccion.getId());
        dto.setPhoneOrigen(transaccion.getPhoneOrigen());
        dto.setPhoneDestino(transaccion.getPhoneDestino());
        dto.setAmount(transaccion.getAmount());
        dto.setStatus(transaccion.getStatus());
        return dto;
    }

    /*public OrchestratorRequestDTO getOrchestratorRequestDTO(OrderRequestDTO orderRequestDTO){
        OrchestratorRequestDTO requestDTO = new OrchestratorRequestDTO();
        requestDTO.setUserId(orderRequestDTO.getUserId());
        requestDTO.setAmount(PRODUCT_PRICE.get(orderRequestDTO.getProductId()));
        requestDTO.setOrderId(orderRequestDTO.getOrderId());
        requestDTO.setProductId(orderRequestDTO.getProductId());
        return requestDTO;
    }*/

}
