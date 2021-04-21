package edu.uoc.easyorderfront.data.table

import edu.uoc.easyorderfront.domain.model.Table

interface TableRepository {
    suspend fun createTable(table: Table): Table?
}