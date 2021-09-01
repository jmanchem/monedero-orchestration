package saga.service.steps;
import com.vinsguru.dto.TransaccionRequestDTO;
import com.vinsguru.dto.TransaccionrResponseDTO;
import com.vinsguru.enums.TransaccionesStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import saga.service.WorkflowStep;
import saga.service.WorkflowStepStatus;
public class TransaccionStep implements WorkflowStep {
    private final WebClient webClient;
    private final TransaccionRequestDTO requestDTO;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;
    public TransaccionStep(WebClient webClient, TransaccionRequestDTO requestDTO) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
    }
    @Override
    public WorkflowStepStatus getStatus() {
        return this.stepStatus;
    }
    @Override
    public Mono<Boolean> process() {
        return this.webClient
                    .post()
                    .uri("/transaccion/process")
                    .body(BodyInserters.fromValue(this.requestDTO))
                    .retrieve()
                    .bodyToMono(TransaccionrResponseDTO.class)
                    .map(r -> r.getStatus().equals(TransaccionesStatus.TRANSACCION_CREATED))
                    .doOnNext(b -> this.stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }
    @Override
    public Mono<Boolean> revert() {
        return this.webClient
                .post()
                .uri("/transaccion/revert")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r -> true)
                .onErrorReturn(false);
    }
}
