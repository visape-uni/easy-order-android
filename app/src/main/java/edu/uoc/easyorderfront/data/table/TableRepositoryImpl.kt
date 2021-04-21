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
}