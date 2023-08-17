/**
 * 
 */
package com.application.tb0823.toolrental.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.tb0823.toolrental.entity.Tool;


/**
 * @author Theodore Belanger
 * Primary Repository for Tools
 */
@Repository
public interface ToolDao extends JpaRepository<Tool, String>{
	
}
