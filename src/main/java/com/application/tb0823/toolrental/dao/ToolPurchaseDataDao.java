package com.application.tb0823.toolrental.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.tb0823.toolrental.entity.ToolPurchaseData;

/**
 * @author Theodore Belanger
 * Primary Repository for ToolPurchaseData
 */
@Repository
public interface ToolPurchaseDataDao extends JpaRepository<ToolPurchaseData, String>{
}
