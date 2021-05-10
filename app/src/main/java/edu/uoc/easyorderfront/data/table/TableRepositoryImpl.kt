package edu.uoc.easyorderfront.data.table

import android.util.Log
import edu.uoc.easyorderfront.domain.model.Table

class TableRepositoryImpl(
    private val tableBackendDataSource: TableBackendDataSource
):TableRepository {
    private val TAG = "TableRepositoryImpl"

    override suspend fun createTable(table: Table): Table? {
        val tableDTO = tableBackendDataSource.createTable(table.convertToDTO())

        Log.d(TAG, "Create table response $tableDTO")

        return tableDTO?.convertToModel()
    }

    override suspend fun getTables(restaurantId: String): List<Table> {
        val listTableDTO = tableBackendDataSource.getTables(restaurantId)

        Log.d(TAG, "Get tables response $listTableDTO")
        val tablesList = listTableDTO.map { table-> table.convertToModel() }.toMutableList()

        return tablesList
    }

    override suspend fun changeTableState(tableId: String, newState: String): Table? {
        val tableDTO = tableBackendDataSource.changeTableState(tableId, TableDTO(tableId, 0, newState))

        Log.d(TAG, "Change table response $tableDTO")

        return tableDTO?.convertToModel()
    }

    override suspend fun getTable(restaurantId: String, tableId: String): Table? {
        val tableDTO = tableBackendDataSource.getTable(restaurantId, tableId)

        Log.d(TAG, "Get table response $tableDTO")

        return tableDTO?.convertToModel()
    }
}