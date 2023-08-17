package com.application.tb0823.toolrental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Theodore Belanger
 * Tool Class
 */
@Table(name="tools")
@Entity(name="Tool")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Tool {
	
	@Id
	@Column(name="toolCode", nullable = false, length = 255)
	private String toolCode;
	
	@Column(name="toolType", nullable = false, length = 255)
	private String toolType;
	
	@Column(name="brand", nullable = false, length = 255)
	private String brand;
}
