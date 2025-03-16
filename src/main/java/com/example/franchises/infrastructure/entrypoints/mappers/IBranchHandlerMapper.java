package com.example.franchises.infrastructure.entrypoints.mappers;

import com.example.franchises.domain.models.Branch;
import com.example.franchises.infrastructure.entrypoints.dtos.branch.BranchResponseDto;
import com.example.franchises.infrastructure.entrypoints.dtos.branch.BranchSaveDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IBranchHandlerMapper {

    Branch toModel (BranchSaveDto branchSaveDto);
    BranchResponseDto toBranchResponseDTO(Branch branch);

}
