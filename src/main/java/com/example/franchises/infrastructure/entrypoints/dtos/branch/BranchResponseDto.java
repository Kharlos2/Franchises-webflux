package com.example.franchises.infrastructure.entrypoints.dtos.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchResponseDto {

    private Long id;
    private String name;

}
