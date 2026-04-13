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
-keepclassmembers enum com.mashiverse.mashit.data.models.sys.wallet.WalletType { *; }

# --- 14. WEB3 HELPER & SINGLETON PROTECTION ---
-keep class com.mashiverse.mashit.utils.helpers.web3.** { *; }
-keepclassmembers class com.mashiverse.mashit.utils.helpers.web3.** { *; }
-keep class com.mashiverse.mashit.utils.helpers.web3.SoldHelper$* { *; }
-keep class com.mashiverse.mashit.utils.ConstantsKt { *; }

# --- 15. REOWN & APPKIT (WALLETCONNECT) ---
# Keep all Reown core and AppKit classes and interfaces
-keep class com.reown.** { *; }
-keep interface com.reown.** { *; }

# AppKit specifically needs protection for its UI, Modal components, and internal routing
-keep class com.reown.appkit.** { *; }
-keep interface com.reown.appkit.** { *; }

# Protect internal SDK storage (SQLDelight/Preference wrappers)
-keep class com.reown.sdk.storage.** { *; }

# Keep models used for JSON-RPC serialization (Session, Account, etc.)
-keepclassmembers class com.reown.appkit.client.models.** { *; }
-keepclassmembers class com.reown.sign.client.models.** { *; }

# Reown uses Kotlin Serialization, Moshi, and Gson transitive dependencies
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
    @com.google.gson.annotations.SerializedName <fields>;
    @kotlinx.serialization.SerialName <fields>;
}

# --- 16. TRANSITIVE WEB3 DEPENDENCIES FOR REOWN ---
# Reown relies on OkHttp WebSockets for the Relay server
-keepclassmembers class okhttp3.OkHttpClient { <fields>; <methods>; }
-keep class okhttp3.internal.ws.** { *; }
-keep interface okhttp3.WebSocket* { *; }

# BouncyCastle is used for cryptographic signing
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

# Protect WalletConnect Relay and Sign protocols
-keep class com.walletconnect.** { *; }
-dontwarn com.walletconnect.**

# --- 17. JNI & NATIVE LIBRARIES ---
# Reown/AppKit uses native libraries for performance-heavy crypto
-keepclasseswithmembers class * {
    native <methods>;
}
-keep class com.reown.** {
    native <methods>;
}

# --- 18. LOGGING & ANNOTATIONS NOISE ---
-dontwarn com.reown.**
-dontwarn com.walletconnect.**
# Avoid R8 warnings for missing optional Reown dependencies
-dontwarn okio.internal.**
-dontwarn reactor.blockhound.integration.BlockHoundIntegration