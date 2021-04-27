package edu.uoc.easyorderfront.data.table

import android.util.Log
import edu.uoc.easyorderfront.data.constants.Endpoints
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import io.ktor.client.*
import io.ktor.client.request.*

class TableBackendDataSource (private val httpClient: HttpClient) {
    private val TAG = "TableBackendDataSource"

    suspend fun createTable(tableDTO: TableDTO): TableDTO? {
        try {
            return httpClient
                .post<TableDTO>(Endpoints.createTableUrl) {
                    body = tableDTO
                }
        } catch (t: Throwable) {
            Log.w(TAG, "Error creando mesa", t)
            //TODO: TRATAR EXCEPCIONES
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }

    suspend fun getTables(restaurantId: String): List<TableDTO> {
        try {
            return httpClient
                .get<List<TableDTO>>(Endpoints.getAllTablesUrl+restaurantId)
        } catch (t: Throwable) {
            //TODO: IF MESSAGE = "Authentication failed, wrong token!" GET NEW ID AND CALL REQUEST AGAIN
            Log.w(TAG, "Error obteniendo mesas", t)
            //TODO: TRATAR EXCEPCIONES
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }
}