package com.example.franchises.infrastructure.adapters.r2dbc.mappers;

import com.example.franchises.domain.models.Franchise;
import com.example.franchises.infrastructure.adapters.r2dbc.entities.FranchiseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IFranchiseEntityMapper {

    FranchiseEntity toFranchiseEntity(Franchise franchise);

    Franchise toFranchiseModel(FranchiseEntity franchiseEntity);

}
