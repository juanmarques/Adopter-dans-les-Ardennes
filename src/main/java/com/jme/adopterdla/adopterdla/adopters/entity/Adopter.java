package com.jme.adopterdla.adopterdla.adopters.entity;

import com.jme.adopterdla.adopterdla.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("adopters")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adopter extends BaseEntity {

    private String processNumber;

}
