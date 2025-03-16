package com.example.franchises.infrastructure.adapters.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("public.franchises")
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseEntity {

    @Id
    private Long id;
    private String name;


}
