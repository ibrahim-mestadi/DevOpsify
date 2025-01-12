package com.code.ebankingbackend.entities;

import com.code.ebankingbackend.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private Date operationDate;
    private double amount;
    private OperationType Type;

    @ManyToOne
    private  BankAccount bankAccount;
}
