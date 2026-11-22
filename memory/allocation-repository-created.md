---
name: allocation-repository-created
description: Created AllocationRepository with findByConsultantId, findByProjectId, and sumOverlappingAllocationPercent methods
metadata:
  type: project
---

Created `AllocationRepository.java` extending `JpaRepository<Allocation, Long>` with three methods:
- `findByConsultantId(Long consultantId)` 
- `findByProjectId(Long projectId)`
- `sumOverlappingAllocationPercent(Long consultantId, LocalDate startDate, LocalDate endDate)` with `@Query`

Build verified: `mvn test` passes.