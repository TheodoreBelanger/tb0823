package com.application.tb0823.toolrental.repo;

import java.util.Hashtable;

import org.springframework.stereotype.Repository;

import com.application.tb0823.toolrental.dao.ToolDao;
import com.application.tb0823.toolrental.dao.ToolPurchaseDataDao;
import com.application.tb0823.toolrental.entity.Tool;
import com.application.tb0823.toolrental.entity.ToolPurchaseData;
import com.application.tb0823.toolrental.exception.CustomDataBaseException;
import com.application.tb0823.toolrental.exception.ToolNotFoundException;
import com.application.tb0823.toolrental.exception.ToolPurchaseDataNotFoundException;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Theodore Belanger
 * Primary Tool Repo for tool and tool purchase data 
 */
@Repository
@Slf4j
@Setter
public class ToolRepository implements ToolRepositoryInterface {

    private Hashtable<String, Tool> toolsTable;
    private Hashtable<String, ToolPurchaseData> toolPurchaseDataTable;
    
    private final ToolDao toolDao;
    private final ToolPurchaseDataDao toolPurchaseDataDao;
    
    /**
     * Tool Repository Constructor
     * @param toolPurchaseDataDao Data Access Object for tool purchase data
     * @param toolDao Data Access Object for tool data
     */
    public ToolRepository(ToolPurchaseDataDao toolPurchaseDataDao, ToolDao toolDao) {
        this.toolDao = toolDao;
        this.toolPurchaseDataDao = toolPurchaseDataDao;
        toolsTable = null;
        toolPurchaseDataTable = null;
    }

    /**
     * Searches tools data
     * @param toolCode
     * @return tool found in internal list of tools
     */
    @Override
    public Tool getTool(String toolCode) {
        if (getToolsTable().containsKey(toolCode)) {
            return getToolsTable().get(toolCode);
        } else {
            String errorString = "Request Error: Tool with tool code " + toolCode + " not found";
            log.error(errorString);
            throw new ToolNotFoundException(errorString);
        }
    }

    /**
     * Searches tool purchase data
     * @param toolType string to search
     * @return toolPurchaseData
     */
    @Override
    public ToolPurchaseData getToolPurchaseData(String toolType) {
        if (getToolPurchaseDataTable().containsKey(toolType)) {
            return getToolPurchaseDataTable().get(toolType);
        } else {
            String errorString = "Server Error: ToolPurchaseData with tool code " + toolType + " not found";
            log.error(errorString);
            throw new ToolPurchaseDataNotFoundException(errorString);
        }
    }
    
    /**
     * Used to clear lazy loaded cache
     */
    @Override
    public void clearCache() {
        toolsTable = null;
        toolPurchaseDataTable = null;
    }
    
    /**
     * lazy load enabled getter for tools table
     * @return tools table
     */
    private Hashtable<String, Tool> getToolsTable() {
        if(toolsTable == null) {
            toolsTable = loadToolsTable();
        }
        return toolsTable;
    }
    
    /**
     * lazy load enabled getter for tool purchase data table
     * @return tool purchase data table
     */
    private Hashtable<String, ToolPurchaseData> getToolPurchaseDataTable() {
        if(toolPurchaseDataTable == null) {
            toolPurchaseDataTable = loadToolPurchaseDataTable();
        }
        return toolPurchaseDataTable;
    }
    
    /**
     * reload tools data from data source
     * @return new loaded tools table from data source
     */
    private Hashtable<String, Tool> loadToolsTable() {
        
        Hashtable<String, Tool> toolsHashtable = new Hashtable<>();

        try {
            for (Tool tool : toolDao.findAll()) {
                toolsHashtable.put(tool.getToolCode(), tool);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomDataBaseException(e.getMessage());
        }

        return toolsHashtable;
    }
    
    /**
     * reload tool purchase data from data source
     * @return new loaded tool purchase data table from data source
     */
    private Hashtable<String, ToolPurchaseData> loadToolPurchaseDataTable() {
         
        Hashtable<String,ToolPurchaseData> tmpToolPurchaseDataTable = new Hashtable<String,ToolPurchaseData>();

        try {
            for (ToolPurchaseData toolPurchaseData : toolPurchaseDataDao.findAll()) {
                tmpToolPurchaseDataTable.put(toolPurchaseData.getToolType(), toolPurchaseData);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomDataBaseException(e.getMessage());
        }

        return tmpToolPurchaseDataTable;
    }
}