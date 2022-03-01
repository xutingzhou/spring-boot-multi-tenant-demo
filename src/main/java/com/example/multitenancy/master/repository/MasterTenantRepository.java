package com.example.multitenancy.master.repository;

import com.example.multitenancy.master.model.MasterTenant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Xutingzhou
 */
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> {
}
