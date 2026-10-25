# Proguard rules for Study Abroad Guide

# Keep domain models
-keep class com.shihab.studyabroad.domain.model.** { *; }

# Room
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
}

# Kotlin Serializer
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Credential Manager & Google ID
-keep class androidx.credentials.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }

# Firebase Auth
-keep class com.google.firebase.auth.** { *; }
