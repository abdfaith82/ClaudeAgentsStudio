# Keep OkHttp and domain models intact
-dontwarn okhttp3.**
-dontwarn okio.**
-keepattributes Signature
-keep class com.damaskitchen.studio.domain.model.** { *; }
