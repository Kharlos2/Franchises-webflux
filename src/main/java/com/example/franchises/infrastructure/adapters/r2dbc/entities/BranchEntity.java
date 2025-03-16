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
@NoArgsConstructor
@AllArgsConstructor
@Table("public.branches")
public class BranchEntity {

    @Id
    private Long id;
    private String name;
    @Column("franchise_id")
    private Long franchiseId;

}
