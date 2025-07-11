package com.stargatex.mobile.lib.pinauth.platform.store.pref

import com.liftric.kvault.KVault
import io.github.aakira.napier.Napier

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
internal class SecurePrefStore(private val store: KVault) : PrefStore {

    override fun save(name: String, stringValue: String?) {
        with(store) {
            if (stringValue != null) set(key = name, stringValue = stringValue) else existsObject(
                forKey = name
            )
        }
    }

    override fun save(name: String, value: Int?) {
        with(store) {
            if (value != null) set(key = name, intValue = value) else existsObject(forKey = name)
        }
    }

    override fun save(name: String, value: Long?) {
        with(store) {
            if (value != null) set(key = name, longValue = value) else existsObject(forKey = name)
        }
    }

    override fun save(name: String, value: Float?) {
        with(store) {
            if (value != null) set(key = name, floatValue = value) else existsObject(forKey = name)
        }
    }

    override fun save(name: String, value: Boolean?) {
        with(store) {
            if (value != null) set(key = name, boolValue = value) else existsObject(forKey = name)
        }
    }

    override fun getString(name: String): String? {
        return store.string(forKey = name)
    }

    override fun getInt(name: String): Int? {
        return store.int(forKey = name)
    }

    override fun getLong(name: String): Long? {
        return store.long(forKey = name)
    }

    override fun getFloat(name: String): Float? {
        return store.float(forKey = name)
    }

    override fun getBoolean(name: String): Boolean? {
        return store.bool(forKey = name)
    }


    override fun additionalParams(paramsHashMap: MutableMap<String, Any?>) {
        Napier.d("paramsHashMap : $paramsHashMap")
        paramsHashMap.forEach { entry ->
            entry.value?.let {
                when (it) {
                    is String -> {
                        store.set(entry.key, it)
                    }

                    is Int -> {
                        store.set(entry.key, it)
                    }

                    is Long -> {
                        store.set(entry.key, it)
                    }

                    is Float -> {
                        store.set(entry.key, it)
                    }

                    is Boolean -> {
                        store.set(entry.key, it)
                    }
                    else -> {
                        throw IllegalStateException("Unknown type for key: ${entry.key} with value: $it")
                    }
                }
            }
        }
    }

    override fun clearAll(): Boolean {
        return store.clear()
    }

}