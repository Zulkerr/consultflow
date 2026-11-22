package com.consultflow.repository;

import com.consultflow.entity.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {

    List<Allocation> findByConsultantId(Long consultantId);

    List<Allocation> findByProjectId(Long projectId);

    @Query("SELECT COALESCE(SUM(a.allocationPercent), 0) FROM Allocation a WHERE a.consultant.id = :consultantId AND a.startDate <= :endDate AND a.endDate >= :startDate")
    int sumOverlappingAllocationPercent(@Param("consultantId") Long consultantId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}