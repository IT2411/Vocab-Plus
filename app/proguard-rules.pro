# ==========================================================================
# Vocab+ — Production ProGuard & R8 Configuration
# ==========================================================================

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,allowobfuscation,allowshrinking class com.vocabplus.app.data.model.** { *; }

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keep class com.vocabplus.app.data.local.entity.** { *; }
-keep interface com.vocabplus.app.data.local.dao.** { *; }

# Jetpack Compose & Material 3
-keepclassmembers class androidx.compose.material3.** {
    public <methods>;
}

# AndroidX WorkManager
-keep class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context, androidx.work.WorkerParameters);
}
-keep class * extends androidx.work.Worker {
    public <init>(android.content.Context, androidx.work.WorkerParameters);
}

# Domain Models (Strict Invariants)
-keep class com.vocabplus.app.domain.model.** { *; }