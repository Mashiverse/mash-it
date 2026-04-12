# ==============================================================================
# MASHIT - FULL PRODUCTION PROGUARD CONFIGURATION (2026)
# ==============================================================================

# --- 1. General Kotlin & Coroutines ---
# Added 'Signature' and 'InnerClasses' here is vital for TypeReference
-keepattributes Signature, InnerClasses, AnnotationDefault, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, EnclosingMethod
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.coroutines.android.HandlerContext {
    volatile <fields>;
}

# --- 2. Networking (Retrofit 3 & OkHttp) ---
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# --- 3. JSON Serialization (GSON / Kotlin Serialization) ---
-keepattributes *Annotation*, InnerClasses, Signature, Exceptions
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepclassmembers class com.mashiverse.mashit.data.models.** {
    *** Companion;
    *** $serializer;
}

# --- 4. DATA MODELS (DTOs & Domain Models) ---
-keep class com.mashiverse.mashit.data.remote.dtos.** { *; }
-keep class com.mashiverse.mashit.data.models.** { *; }
-keepclassmembers class com.mashiverse.mashit.data.models.** {
    <fields>;
    <methods>;
}

# --- 5. Paging 3 ---
-keep class androidx.paging.** { *; }
-dontwarn androidx.paging.**

# --- 6. WEB3J & BLOCKCHAIN (CRITICAL FIX) ---
# Keep the ENTIRE web3j library. Web3j uses heavy reflection across all subpackages
# (TypeDecoder, TypeEncoder, Utils, synthetic inner classes, etc). Targeting
# individual subpackages causes R8 to keep finding new things to strip.
-keepattributes Signature, InnerClasses, EnclosingMethod
-keep class org.web3j.** { *; }
-keep interface org.web3j.** { *; }
-keep class com.coinbase.** { *; }
-dontwarn com.coinbase.**
-dontwarn org.web3j.**

# --- 7. Dependency Injection (Hilt) ---
-keep class dagger.hilt.** { *; }
-keep class com.google.dagger.** { *; }
-dontwarn hilt.internal.processor.**

# --- 8. Android Specifics & Parcelable ---
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# --- 9. ENUM & UI STABILITY ---
-keep enum * { *; }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    public ** name();
}
-keep class com.mashiverse.mashit.ui.default.traits.** { *; }
-keep class com.mashiverse.mashit.ui.screens.mashup.categories.sections.** { *; }

# --- 10. NATIVE LIBRARIES ---
-keepclasseswithmembers class * {
    native <methods>;
}
-keep class com.mashiverse.mashit.** {
    native <methods>;
}

# --- 11. HARDWARE & LOGGING NOISE ---
-dontwarn android.hardware.**
-dontwarn com.qualcomm.**
-dontwarn com.google.android.gms.internal.**

# --- 12. MISSING CLASSES FIX ---
-dontwarn io.netty.internal.tcnative.**
-dontwarn io.vertx.codegen.annotations.VertxGen
-dontwarn org.apache.log4j.**
-dontwarn org.apache.logging.log4j.**
-dontwarn org.slf4j.**
-dontwarn org.bouncycastle.**
-dontwarn com.google.errorprone.annotations.**
-dontwarn org.checkerframework.**
-dontwarn javax.annotation.**
-dontwarn androidx.test.**

# --- 13. SPECIFIC ENUM PROTECTION ---
-keepclassmembers enum com.mashiverse.mashit.data.models.mashi.PriceCurrency { *; }
-keepclassmembers enum com.mashiverse.mashit.data.models.mashi.TraitType { *; }
-keepclassmembers enum com.mashiverse.mashit.data.models.sys.screens.ScreenInfo { *; }

# --- 14. WEB3 HELPER & SINGLETON PROTECTION ---
-keep class com.mashiverse.mashit.utils.helpers.web3.** { *; }
-keepclassmembers class com.mashiverse.mashit.utils.helpers.web3.** { *; }
-keep class com.mashiverse.mashit.utils.helpers.web3.SoldHelper$* { *; }
-keep class com.mashiverse.mashit.utils.ConstantsKt { *; }