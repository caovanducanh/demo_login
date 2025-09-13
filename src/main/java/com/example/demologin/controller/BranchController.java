package com.example.demologin.controller;

import com.example.demologin.annotation.ApiResponse;
import com.example.demologin.annotation.PublicEndpoint;
import com.example.demologin.annotation.SecuredEndpoint;
import com.example.demologin.annotation.UserActivity;
import com.example.demologin.dto.request.branch.CreateBranchRequest;
import com.example.demologin.dto.request.branch.UpdateBranchRequest;
import com.example.demologin.dto.response.BranchResponse;
import com.example.demologin.enums.ActivityType;
import com.example.demologin.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
@Tag(name = "Branch Management", description = "APIs for managing branches and branch selection")
public class BranchController {
    
    private final BranchService branchService;
    
    @PublicEndpoint
    @GetMapping
    @Operation(summary = "Get all active branches", 
               description = "Get list of all active branches available for selection")
    public List<BranchResponse> getAllActiveBranches() {
        return branchService.getAllActiveBranches();
    }
    
    
    // Note: No need for separate select and oauth2-url endpoints
    // Frontend should directly redirect to /oauth2/authorization/google?branch=HCM
    
    @PublicEndpoint
    @GetMapping("/validate-email")
    @Operation(summary = "Validate email for branch", 
               description = "Check if email is allowed for selected branch")
    public Object validateEmailForBranch(
            @Parameter(description = "Email address") @RequestParam String email,
            @Parameter(description = "Branch code") @RequestParam String branchCode) {
        
        return branchService.validateEmailForBranch(email, branchCode);
    }
    
    @SecuredEndpoint("BRANCH_CREATE")
    @PostMapping
    @ApiResponse(message = "Branch created successfully")
    @UserActivity(activityType = ActivityType.ADMIN_ACTION, details = "Branch creation")
    @Operation(summary = "Create new branch", 
               description = "Create a new branch (admin only)")
    public Object createBranch(@RequestBody @Valid CreateBranchRequest request) {
        return branchService.createBranch(request);
    }
    
    @SecuredEndpoint("BRANCH_READ")
    @GetMapping("/{id}")
    @ApiResponse(message = "Branch retrieved successfully")
    @Operation(summary = "Get branch by ID", 
               description = "Get branch details by ID")
    public Object getBranchById(@PathVariable Long id) {
        return branchService.getBranchById(id);
    }
    
    @SecuredEndpoint("BRANCH_UPDATE")
    @PutMapping("/{id}")
    @ApiResponse(message = "Branch updated successfully")
    @UserActivity(activityType = ActivityType.ADMIN_ACTION, details = "Branch update")
    @Operation(summary = "Update branch", 
               description = "Update branch information (admin only)")
    public Object updateBranch(
            @PathVariable Long id, 
            @RequestBody @Valid UpdateBranchRequest request) {
        return branchService.updateBranch(id, request);
    }
    
    @SecuredEndpoint("BRANCH_DELETE")
    @DeleteMapping("/{id}")
    @ApiResponse(message = "Branch deactivated successfully")
    @UserActivity(activityType = ActivityType.ADMIN_ACTION, details = "Branch deactivation")
    @Operation(summary = "Deactivate branch", 
               description = "Deactivate a branch (admin only)")
    public void deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
    }
    
    // Email management endpoints
    
    @PostMapping("/{branchId}/allowed-emails")
    @SecuredEndpoint("BRANCH_UPDATE")
    @UserActivity(activityType = ActivityType.ADMIN_ACTION, details = "Added allowed email to branch")
    @Operation(summary = "Add allowed email to branch")
    @ApiResponse
    public void addAllowedEmail(
            @PathVariable Long branchId,
            @RequestParam String email,
            @RequestParam(required = false) String description) {
        branchService.addAllowedEmailToBranch(branchId, email, description);
    }
    
    @DeleteMapping("/{branchId}/allowed-emails")
    @SecuredEndpoint("BRANCH_UPDATE")
    @UserActivity(activityType = ActivityType.ADMIN_ACTION, details = "Removed allowed email from branch")
    @Operation(summary = "Remove allowed email from branch")
    @ApiResponse
    public void removeAllowedEmail(
            @PathVariable Long branchId,
            @RequestParam String email) {
        branchService.removeAllowedEmailFromBranch(branchId, email);
    }
    
    @GetMapping("/{branchId}/allowed-emails")
    @SecuredEndpoint("BRANCH_READ")
    @Operation(summary = "Get allowed emails for branch")
    @ApiResponse
    public Object getAllowedEmails(@PathVariable Long branchId) {
        return branchService.getAllowedEmailsForBranch(branchId);
    }
}
