package com.library.base.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

object DataStoreUtils {

    /**
     * 保存数据
     * */
    suspend fun <T : Any> put(context: Context, key: String, value: T) {
        context.dataStore.edit {
            when (value) {
                is Int -> it[intPreferencesKey(key)] = value
                is Long -> it[longPreferencesKey(key)] = value
                is Double -> it[doublePreferencesKey(key)] = value
                is Float -> it[floatPreferencesKey(key)] = value
                is Boolean -> it[booleanPreferencesKey(key)] = value
                is String -> it[stringPreferencesKey(key)] = value
                else -> throw IllegalArgumentException("This type can be saved into DataStore")
            }
        }
    }

    /**
     * 获取数据
     * */
    suspend inline fun <reified T : Any> get(context: Context, key: String): T {
        return when (T::class) {
            Int::class -> {
                context.dataStore.data.map { setting ->
                    setting[intPreferencesKey(key)] ?: 0
                }.first() as T
            }
            Long::class -> {
                context.dataStore.data.map { setting ->
                    setting[longPreferencesKey(key)] ?: 0L
                }.first() as T
            }
            Double::class -> {
                context.dataStore.data.map { setting ->
                    setting[doublePreferencesKey(key)] ?: 0.0
                }.first() as T
            }
            Float::class -> {
                context.dataStore.data.map { setting ->
                    setting[floatPreferencesKey(key)] ?: 0f
                }.first() as T
            }
            Boolean::class -> {
                context.dataStore.data.map { setting ->
                    setting[booleanPreferencesKey(key)] ?: false
                }.first() as T
            }
            String::class -> {
                context.dataStore.data.map { setting ->
                    setting[stringPreferencesKey(key)] ?: ""
                }.first() as T
            }
            else -> {
                throw IllegalArgumentException("This type can be get into DataStore")
            }
        }
    }
}

