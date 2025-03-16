package com.example.franchises.infrastructure.adapters.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.data.relational.core.mapping.Column;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockBranchProductEntity {
    @Column("branch_name")
    private String branchName;
    @Column("product_name")
    private String productName;
    private Integer stock;

}
