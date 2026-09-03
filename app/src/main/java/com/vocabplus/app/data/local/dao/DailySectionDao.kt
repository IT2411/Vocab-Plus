package com.vocabplus.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vocabplus.app.data.local.entity.DailySectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailySectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(section: DailySectionEntity)

    @Query("SELECT * FROM daily_sections WHERE date_iso = :dateIso AND category_slug = :categorySlug")
    suspend fun getSection(dateIso: String, categorySlug: String): DailySectionEntity?

    @Query("SELECT * FROM daily_sections WHERE date_iso = :dateIso")
    fun getSectionsForDateFlow(dateIso: String): Flow<List<DailySectionEntity>>

    @Query("SELECT * FROM daily_sections WHERE date_iso = :dateIso")
    suspend fun getSectionsForDate(dateIso: String): List<DailySectionEntity>
}