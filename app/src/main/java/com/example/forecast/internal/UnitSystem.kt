package com.example.forecast.internal

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

enum class UnitSystem {
    METRIC,US;

    @JsonValue
    open fun getUnitName(): String? {
        return toString().lowercase(Locale.ENGLISH)
    }
}