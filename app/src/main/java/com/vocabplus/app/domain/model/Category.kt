package com.vocabplus.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Category(val displayName: String, val slug: String) {
    SYNONYM("Synonyms", "synonym"),
    ANTONYM("Antonyms", "antonym"),
    IDIOM("Idioms", "idiom");

    companion object {
        fun fromSlug(slug: String): Category? =
            entries.find { it.slug.equals(slug, ignoreCase = true) }
    }
}