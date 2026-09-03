package com.vocabplus.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_sections",
    indices = [
        Index(value = ["date_iso", "category_slug"], unique = true)
    ]
)
data class DailySectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "date_iso") val dateIso: String,
    @ColumnInfo(name = "category_slug") val categorySlug: String,
    @ColumnInfo(name = "state") val state: String, // "NOT_STARTED", "IN_PROGRESS", "COMPLETED"
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "total_points") val totalPoints: Int,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean
)