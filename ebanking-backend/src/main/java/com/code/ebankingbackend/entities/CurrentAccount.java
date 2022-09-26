package com.code.ebankingbackend.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue("CC")
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAccount extends AccountOperation {
    private double overDraft;
}
