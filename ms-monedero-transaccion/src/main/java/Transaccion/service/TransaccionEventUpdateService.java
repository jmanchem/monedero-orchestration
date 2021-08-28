package Transaccion.service;

import com.vinsguru.dto.OrchestratorResponseDTO;
import Transaccion.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TransaccionEventUpdateService {

    @Autowired
    private TransaccionRepository repository;
/*
    @Transactional
    public void updateOrder(final OrchestratorResponseDTO responseDTO){
        this.repository
                .findById(responseDTO.getOrderId())
                .ifPresent(po -> {
                    po.setStatus(responseDTO.getStatus());
                    this.repository.save(po);
                });
    }*/

}
