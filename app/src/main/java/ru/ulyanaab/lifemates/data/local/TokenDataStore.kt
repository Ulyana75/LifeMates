package ru.ulyanaab.lifemates.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ru.ulyanaab.lifemates.domain.common.model.TokensModel
import javax.inject.Inject

class TokenDataStore @Inject constructor(
    private val context: Context,
) {

    suspend fun get(): TokensModel {
        return context.dataStore.data
            .map {
                TokensModel(
                    accessToken = it[ACCESS_TOKEN_KEY] ?: "",
                    refreshToken = it[REFRESH_TOKEN_KEY] ?: ""
                )
            }
            .firstOrNull()
            ?: TokensModel.EMPTY
    }

    suspend fun put(model: TokensModel) {
        context.dataStore.edit {
            it[ACCESS_TOKEN_KEY] = model.accessToken
            it[REFRESH_TOKEN_KEY] = model.refreshToken
        }
    }

    suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private const val DATASTORE_NAME = "lifemates_tokens_datastore"
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

        private val Context.dataStore by preferencesDataStore(
            name = DATASTORE_NAME
        )
    }
}
