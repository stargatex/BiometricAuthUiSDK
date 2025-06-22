package com.stargatex.mobile.lib.pinauth.platform.store.pref

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
interface PrefStore {
    fun save(name: String, stringValue: String?)
    fun save(name: String, intValue: Int?)
    fun save(name: String, longValue: Long?)
    fun save(name: String, flotValue: Float?)
    fun save(name: String, boolValue: Boolean?)

    fun getString(name: String): String?
    fun getInt(name: String): Int?
    fun getLong(name: String): Long?
    fun getFloat(name: String): Float?
    fun getBoolean(name: String): Boolean?

    fun additionalParams(paramsHashMap: MutableMap<String, Any?>)

    fun clearAll(): Boolean
}