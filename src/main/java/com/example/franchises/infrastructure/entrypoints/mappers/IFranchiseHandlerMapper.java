package com.example.franchises.infrastructure.entrypoints.mappers;

import com.example.franchises.domain.models.Franchise;
import com.example.franchises.infrastructure.entrypoints.dtos.franchise.FranchiseResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.franchise.FranchiseSaveDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IFranchiseHandlerMapper {

    Franchise toModel (FranchiseSaveDto franchiseSaveDto);
    FranchiseResponseDto toFranchiseResponseDTO (Franchise franchise);

}
