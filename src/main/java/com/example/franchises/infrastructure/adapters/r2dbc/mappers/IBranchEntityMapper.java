package com.example.franchises.infrastructure.adapters.r2dbc.mappers;

import com.example.franchises.domain.models.Branch;
import com.example.franchises.infrastructure.adapters.r2dbc.entities.BranchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IBranchEntityMapper {

    BranchEntity toBranchEntity(Branch branch);

    Branch toBranchModel(BranchEntity branchEntity);

}
