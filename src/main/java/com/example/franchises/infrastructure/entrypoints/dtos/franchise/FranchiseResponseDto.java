package com.example.franchises.infrastructure.entrypoints.dtos.franchise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseResponseDto {
    private Long id;
    private String name;

}
