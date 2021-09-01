package saga.service;

import com.vinsguru.dto.*;
import com.vinsguru.enums.OrderStatus;
import com.vinsguru.enums.TransaccionesStatus;
import org.springframework.web.bind.annotation.RequestParam;
import saga.service.steps.InventoryStep;
import saga.service.steps.PaymentStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import saga.service.steps.TransaccionStep;

import java.util.List;

@Service
public class OrchestratorService {

    @Autowired
    @Qualifier("payment")
    private WebClient paymentClient;

    @Autowired
    @Qualifier("inventory")
    private WebClient inventoryClient;

    @Autowired
    @Qualifier("transaccion")
    private  WebClient transaacionClient;

    public Mono<OrchestratorResponseDTO> transaccionTransfer (final OrchestratorRequestDTO requestDTO){
        Workflow transaccionWorkFlow = this.getTransaccionWorkflow(requestDTO);
        return Flux.fromStream(() -> transaccionWorkFlow.getSteps().stream())
                .flatMap(WorkflowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if(aBoolean)
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new WorkflowException("create Transaccion failed!"));
                }))
                .then(Mono.fromCallable(() -> getResponseDTOT(requestDTO, TransaccionesStatus.TRANSACCION_CREATED)))
                .onErrorResume(ex -> this.revertTransaccion(transaccionWorkFlow, requestDTO));
    }
    private Workflow getTransaccionWorkflow(OrchestratorRequestDTO requestDTO){
        WorkflowStep transaccionStep = new TransaccionStep(this.transaacionClient, this.getTransaccionRequestDTO(requestDTO));
        return new OrderWorkflow(List.of(transaccionStep));

    }
    private TransaccionRequestDTO getTransaccionRequestDTO(OrchestratorRequestDTO requestDTO){
        TransaccionRequestDTO transaccionRequestDTO = new TransaccionRequestDTO();
        transaccionRequestDTO.setTransaccionId(requestDTO.getTransaccionId());
        transaccionRequestDTO.setAmount(requestDTO.getAmount());
        return transaccionRequestDTO;
    }
    private Mono<OrchestratorResponseDTO> revertTransaccion(final Workflow workflow, final OrchestratorRequestDTO requestDTO){
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .flatMap(WorkflowStep::revert)
                .retry(3)
                .then(Mono.just(this.getResponseDTOT(requestDTO, TransaccionesStatus.TRANSACCION_CANCELLED)));
    }
    private OrchestratorResponseDTO getResponseDTOT(OrchestratorRequestDTO requestDTO, TransaccionesStatus status){
        OrchestratorResponseDTO responseDTO = new OrchestratorResponseDTO();
        responseDTO.setTransaccionId(requestDTO.getTransaccionId());
        responseDTO.setAmount(requestDTO.getAmount());
        responseDTO.setStatusT(status);
        return responseDTO;
    }
    ////////////////////////////////////////////////////////////////////////
    public Mono<OrchestratorResponseDTO> orderProduct(final OrchestratorRequestDTO requestDTO){
        Workflow orderWorkflow = this.getOrderWorkflow(requestDTO);
        return Flux.fromStream(() -> orderWorkflow.getSteps().stream())
                .flatMap(WorkflowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if(aBoolean)
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new WorkflowException("create Transaccion failed!"));
                }))
                .then(Mono.fromCallable(() -> getResponseDTO(requestDTO, OrderStatus.ORDER_COMPLETED)))
                .onErrorResume(ex -> this.revertOrder(orderWorkflow, requestDTO));

    }

    private Mono<OrchestratorResponseDTO> revertOrder(final Workflow workflow, final OrchestratorRequestDTO requestDTO){
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .flatMap(WorkflowStep::revert)
                .retry(3)
                .then(Mono.just(this.getResponseDTO(requestDTO, OrderStatus.ORDER_CANCELLED)));
    }

    private Workflow getOrderWorkflow(OrchestratorRequestDTO requestDTO){
        WorkflowStep paymentStep = new PaymentStep(this.paymentClient, this.getPaymentRequestDTO(requestDTO));
        WorkflowStep inventoryStep = new InventoryStep(this.inventoryClient, this.getInventoryRequestDTO(requestDTO));
        return new OrderWorkflow(List.of(paymentStep, inventoryStep));
    }

    private OrchestratorResponseDTO getResponseDTO(OrchestratorRequestDTO requestDTO, OrderStatus status){
        OrchestratorResponseDTO responseDTO = new OrchestratorResponseDTO();
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setAmount(requestDTO.getAmount());
        responseDTO.setProductId(requestDTO.getProductId());
        responseDTO.setUserId(requestDTO.getUserId());
        responseDTO.setStatus(status);
        return responseDTO;
    }

    private PaymentRequestDTO getPaymentRequestDTO(OrchestratorRequestDTO requestDTO){
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setUserId(requestDTO.getUserId());
        paymentRequestDTO.setAmount(requestDTO.getAmount());
        paymentRequestDTO.setOrderId(requestDTO.getOrderId());
        return paymentRequestDTO;
    }

    private InventoryRequestDTO getInventoryRequestDTO(OrchestratorRequestDTO requestDTO){
        InventoryRequestDTO inventoryRequestDTO = new InventoryRequestDTO();
        inventoryRequestDTO.setUserId(requestDTO.getUserId());
        inventoryRequestDTO.setProductId(requestDTO.getProductId());
        inventoryRequestDTO.setOrderId(requestDTO.getOrderId());
        return inventoryRequestDTO;
    }

}
