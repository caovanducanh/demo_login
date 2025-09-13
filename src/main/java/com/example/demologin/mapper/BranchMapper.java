package com.example.demologin.mapper;

import com.example.demologin.dto.request.branch.CreateBranchRequest;
import com.example.demologin.dto.response.BranchResponse;
import com.example.demologin.entity.Branch;
import com.example.demologin.entity.BranchAllowedEmail;

import java.util.List;
import java.util.stream.Collectors;

public class BranchMapper {
    
    public static BranchResponse toBranchResponse(Branch branch) {
        if (branch == null) {
            return null;
        }
        
        BranchResponse response = new BranchResponse();
        response.setId(branch.getId());
        response.setName(branch.getName());
        response.setCode(branch.getCode());
        response.setAddress(branch.getAddress());
        
        // Get allowed emails from the relationship
        List<String> allowedEmails = branch.getAllowedEmails() != null ? 
                branch.getAllowedEmails().stream()
                        .filter(BranchAllowedEmail::getActive)
                        .map(BranchAllowedEmail::getEmail)
                        .collect(Collectors.toList()) : 
                List.of();
        response.setAllowedEmails(allowedEmails);
        
        response.setActive(branch.getActive());
        response.setCreatedAt(branch.getCreatedAt());
        
        return response;
    }
    
    public static Branch toEntity(CreateBranchRequest request) {
        if (request == null) {
            return null;
        }
        
        return Branch.builder()
                .name(request.getName())
                .code(request.getCode())
                .address(request.getAddress())
                .active(request.getActive())
                .build();
    }
}
