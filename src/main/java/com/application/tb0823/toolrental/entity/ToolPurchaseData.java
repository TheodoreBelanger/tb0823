package com.application.tb0823.toolrental.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * @author Theodore Belanger
 * Tool Purchase Data Class
 */
@Table(name="toolpurchasedata")
@Entity(name="ToolPurchaseData")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ToolPurchaseData {
    @Id
    @Column(name="toolType", nullable = false)
    private String toolType;
    
    @Column(name="dailyCharge", nullable = false)
    private BigDecimal dailyCharge;
    
    @Column(name="weekdayCharge", nullable = false)
    private boolean weekdayCharge;
    
    @Column(name="weekendCharge", nullable = false)
    private boolean weekendCharge;
    
    @Column(name="holidayCharge", nullable = false)
    private boolean holidayCharge;
}
