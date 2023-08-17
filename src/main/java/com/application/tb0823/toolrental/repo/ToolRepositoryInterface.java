package com.application.tb0823.toolrental.repo;

import com.application.tb0823.toolrental.entity.Tool;
import com.application.tb0823.toolrental.entity.ToolPurchaseData;

/**
 * @author Theodore Belanger
 * ToolRepositoryInterface
 */
public interface ToolRepositoryInterface {

	/**
	 * Searches tools data
	 * @param toolCode
	 * @return tool found in internal list of tools
	 */
	Tool getTool(String toolCode);

	/**
	 * Searches tool purchase data
	 * @param toolType string to search
	 * @return toolPurchaseData
	 */
	ToolPurchaseData getToolPurchaseData(String toolType);

	/**
	 * Used to clear lazy loaded cache
	 */
	void clearCache();

}