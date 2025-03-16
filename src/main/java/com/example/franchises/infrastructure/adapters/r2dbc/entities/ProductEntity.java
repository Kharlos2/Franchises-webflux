package com.example.franchises.infrastructure.adapters.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("public.products")
public class ProductEntity {

    @Id
    private Long id;
    private String name;
    private Integer stock;
    @Column("branch_id")
    private Long branchId;

}
