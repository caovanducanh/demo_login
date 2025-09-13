package com.example.demologin.repository;

import com.example.demologin.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    
    Optional<Branch> findByCode(String code);
    
    Optional<Branch> findByName(String name);
    
    List<Branch> findByActiveTrue();
    
    @Query("SELECT DISTINCT bae.branch FROM BranchAllowedEmail bae WHERE bae.email = :email AND bae.active = true AND bae.branch.active = true")
    Optional<Branch> findByAllowedEmail(@Param("email") String email);
}
