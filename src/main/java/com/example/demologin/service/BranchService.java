package com.example.demologin.service;

import com.example.demologin.dto.request.branch.CreateBranchRequest;
import com.example.demologin.dto.request.branch.UpdateBranchRequest;
import com.example.demologin.dto.response.BranchResponse;
import com.example.demologin.entity.Branch;

import java.util.List;

public interface BranchService {
    
    List<BranchResponse> getAllActiveBranches();
    
    BranchResponse getBranchById(Long id);
    
    BranchResponse getBranchByCode(String code);
    
    BranchResponse createBranch(CreateBranchRequest request);
    
    BranchResponse updateBranch(Long id, UpdateBranchRequest request);
    
    void deleteBranch(Long id);
    
    boolean isEmailAllowedForBranch(String email, Long branchId);
    
    Branch findBranchByAllowedEmail(String email);
    
    boolean validateEmailForBranch(String email, String branchCode);
    
    Branch getBranchEntityByCode(String code);
    
    // New methods for managing allowed emails
    void addAllowedEmailToBranch(Long branchId, String email, String description);
    
    void removeAllowedEmailFromBranch(Long branchId, String email);
    
    List<String> getAllowedEmailsForBranch(Long branchId);
}
