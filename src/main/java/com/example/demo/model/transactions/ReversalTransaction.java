package com.example.demo.model.transactions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class ReversalTransaction extends Transaction{


    @Column(name = "is_invalid")
    private boolean isInvalid;
}
