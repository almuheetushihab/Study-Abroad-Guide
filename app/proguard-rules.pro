# Proguard rules for Study Abroad Guide

# Keep domain models
-keep class com.yourname.studyabroad.domain.model.** { *; }

# Room
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
}

# Kotlin Serializer
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
