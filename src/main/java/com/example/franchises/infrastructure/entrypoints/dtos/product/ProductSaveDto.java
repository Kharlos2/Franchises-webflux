package com.example.franchises.infrastructure.entrypoints.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveDto {

    private String name;
    private Integer stock;
    private Long branchId;

}
