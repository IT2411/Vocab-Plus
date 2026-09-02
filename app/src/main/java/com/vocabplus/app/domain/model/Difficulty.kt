package com.vocabplus.app.domain.model

enum class Difficulty(val level: Int, val label: String) {
    FAMILIAR(1, "Familiar"),
    MODERATE(2, "Moderate"),
    CHALLENGING(3, "Challenging"),
    DIFFICULT(4, "Difficult"),
    ADVANCED(5, "Advanced");

    companion object {
        fun fromLevel(level: Int): Difficulty =
            entries.find { it.level == level } ?: MODERATE
    }
}