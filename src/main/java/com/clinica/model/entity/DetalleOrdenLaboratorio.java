package com.clinica.model.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.math.BigDecimal;

@Embeddable
@Data
public class DetalleOrdenLaboratorio {
    private String tipoTrabajo;
    private String piezasDentales;
    private String colorGuiaVita;
    private BigDecimal costo;
}