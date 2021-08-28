package Transaccion.entity;

import com.vinsguru.enums.OrderStatus;
import com.vinsguru.enums.TransaccionesStatus;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@ToString
public class Transaccion {
    @Id
    private UUID id;
    private String phoneOrigen;
    private String phoneDestino;
    private Double amount;
    private TransaccionesStatus status;

}