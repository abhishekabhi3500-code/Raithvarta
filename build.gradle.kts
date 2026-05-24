plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")  // ← ADD THIS
}

// ... keep all existing android{} block same ...

dependencies {
    // ... keep existing dependencies ...

    // Firebase BOM (manages all versions)
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    // Firebase services you need
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
}