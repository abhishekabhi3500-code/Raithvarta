package com.example.raithavarta

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseService {

    private val db = FirebaseFirestore.getInstance()

    // Fetch tips from Firestore
    suspend fun fetchTips(): List<FarmingTip> {
        return try {
            val snapshot = db.collection("tips").get().await()
            snapshot.documents.mapNotNull { doc ->
                FarmingTip(
                    crop     = doc.getString("crop") ?: "",
                    titleEn  = doc.getString("titleEn") ?: "",
                    titleKn  = doc.getString("titleKn") ?: "",
                    tipEn    = doc.getString("tipEn") ?: "",
                    tipKn    = doc.getString("tipKn") ?: "",
                    imageUrl = doc.getString("imageUrl") ?: "",
                    emoji    = doc.getString("emoji") ?: "🌾"
                )
            }
        } catch (e: Exception) {
            emptyList() // fallback to local if no internet
        }
    }

    // Fetch stories from Firestore
    suspend fun fetchStories(): List<SuccessStory> {
        return try {
            val snapshot = db.collection("stories").get().await()
            snapshot.documents.mapNotNull { doc ->
                SuccessStory(
                    farmerName = doc.getString("farmerName") ?: "",
                    village    = doc.getString("village") ?: "",
                    crop       = doc.getString("crop") ?: "",
                    storyEn    = doc.getString("storyEn") ?: "",
                    storyKn    = doc.getString("storyKn") ?: "",
                    resultEn   = doc.getString("resultEn") ?: "",
                    resultKn   = doc.getString("resultKn") ?: "",
                    emoji      = doc.getString("emoji") ?: "🌾"
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Submit expert query
    suspend fun submitExpertQuery(question: String, crop: String): Boolean {
        return try {
            val query = hashMapOf(
                "question"  to question,
                "crop"      to crop,
                "status"    to "pending",
                "timestamp" to com.google.firebase.Timestamp.now()
            )
            db.collection("expertQueries").add(query).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}