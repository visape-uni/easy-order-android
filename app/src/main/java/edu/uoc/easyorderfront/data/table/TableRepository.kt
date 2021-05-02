package edu.uoc.easyorderfront.data.table

import edu.uoc.easyorderfront.domain.model.Table

interface TableRepository {
    suspend fun createTable(table: Table): Table?
    suspend fun getTables(restaurantId: String): List<Table>
    suspend fun changeTableState(tableId: String, newState: String): Table?
}