package com.example.raithavarta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.raithavarta.ui.theme.RaithaVartaTheme

// ─── DATA MODELS ────────────────────────────────────────────
data class FarmingTip(
    val crop: String,
    val titleEn: String,
    val titleKn: String,
    val tipEn: String,
    val tipKn: String,
    val imageUrl: String,
    val emoji: String
)

data class SuccessStory(
    val farmerName: String,
    val village: String,
    val crop: String,
    val storyEn: String,
    val storyKn: String,
    val resultEn: String,
    val resultKn: String,
    val emoji: String
)

// ─── ALL TIPS ───────────────────────────────────────────────
val tips = listOf(
    FarmingTip("Paddy", "Paddy Pest Control", "ಭತ್ತ ಕೀಟ ನಿಯಂತ್ರಣ",
        "Spray neem oil early morning to control leaf folder pests. Repeat every 10 days.",
        "ಎಲೆ ಮಡಚುವ ಕೀಟಗಳನ್ನು ನಿಯಂತ್ರಿಸಲು ಬೆಳಿಗ್ಗೆ ಬೇವಿನ ಎಣ್ಣೆ ಸಿಂಪಡಿಸಿ. ಪ್ರತಿ 10 ದಿನಗಳಿಗೊಮ್ಮೆ ಪುನರಾವರ್ತಿಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Rice_p1160004.jpg/640px-Rice_p1160004.jpg", "🌾"),
    FarmingTip("Paddy", "Best Weeding Time", "ಕಳೆ ತೆಗೆಯಲು ಸರಿಯಾದ ಸಮಯ",
        "Weed paddy fields between 15–25 days after transplanting for best yield.",
        "ನಾಟಿ ಮಾಡಿದ 15-25 ದಿನಗಳ ನಡುವೆ ಭತ್ತದ ಗದ್ದೆ ಕಳೆ ತೆಗೆಯಿರಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Rice_p1160004.jpg/640px-Rice_p1160004.jpg", "🌾"),
    FarmingTip("Paddy", "Water Management", "ನೀರು ನಿರ್ವಹಣೆ",
        "Maintain 5cm water level during tillering stage. Drain water 10 days before harvest.",
        "ಕಂಸ ಹಂತದಲ್ಲಿ 5 ಸೆಂ.ಮೀ ನೀರಿನ ಮಟ್ಟ ಕಾಪಾಡಿ. ಕೊಯ್ಲಿಗೆ 10 ದಿನ ಮೊದಲು ನೀರು ತೆಗೆಯಿರಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Rice_p1160004.jpg/640px-Rice_p1160004.jpg", "🌾"),
    FarmingTip("Tomato", "Tomato Watering Tip", "ಟೊಮೆಟೊ ನೀರಾವರಿ ಸಲಹೆ",
        "Water tomatoes at the base, never on leaves. Best time is early morning to prevent fungal disease.",
        "ಟೊಮೆಟೊಗೆ ಬುಡದಲ್ಲಿ ನೀರು ಹಾಕಿ, ಎಲೆಗಳ ಮೇಲೆ ಹಾಕಬೇಡಿ. ಶಿಲೀಂಧ್ರ ತಡೆಯಲು ಬೆಳಿಗ್ಗೆ ನೀರು ಹಾಕಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Tomato_je.jpg/640px-Tomato_je.jpg", "🍅"),
    FarmingTip("Tomato", "Tomato Staking", "ಟೊಮೆಟೊ ಆಧಾರ ಕೋಲು",
        "Stake tomato plants when they reach 30cm height. This prevents stem breakage.",
        "ಟೊಮೆಟೊ ಗಿಡ 30 ಸೆಂ.ಮೀ ಎತ್ತರ ಆದಾಗ ಆಧಾರ ಕೋಲು ಹಾಕಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Tomato_je.jpg/640px-Tomato_je.jpg", "🍅"),
    FarmingTip("Tomato", "Blight Prevention", "ರೋಗ ತಡೆಗಟ್ಟುವಿಕೆ",
        "Spray Bordeaux mixture (1%) to prevent early blight in tomatoes during rainy season.",
        "ಮಳೆಗಾಲದಲ್ಲಿ ಟೊಮೆಟೊ ರೋಗ ತಡೆಯಲು ಬೋರ್ಡೋ ಮಿಶ್ರಣ (1%) ಸಿಂಪಡಿಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Tomato_je.jpg/640px-Tomato_je.jpg", "🍅"),
    FarmingTip("Coconut", "Coconut Fertilizer", "ತೆಂಗು ಗೊಬ್ಬರ ಸಲಹೆ",
        "Apply organic compost around the base of coconut trees before monsoon. Use 50kg per tree.",
        "ಮಳೆಗಾಲದ ಮೊದಲು ತೆಂಗು ಮರದ ಬುಡಕ್ಕೆ ಸಾವಯವ ಗೊಬ್ಬರ ಹಾಕಿ. ಪ್ರತಿ ಮರಕ್ಕೆ 50 ಕೆಜಿ ಬಳಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Coconut_on_tree.jpg/640px-Coconut_on_tree.jpg", "🥥"),
    FarmingTip("Coconut", "Coconut Root Feeding", "ತೆಂಗು ಬೇರಿಗೆ ಗೊಬ್ಬರ",
        "Mix 2kg urea + 3kg super phosphate + 3kg potash per tree annually for healthy growth.",
        "ಆರೋಗ್ಯಕರ ಬೆಳವಣಿಗೆಗೆ ವಾರ್ಷಿಕ 2ಕೆಜಿ ಯೂರಿಯಾ + 3ಕೆಜಿ ಸೂಪರ್ ಫಾಸ್ಫೇಟ್ + 3ಕೆಜಿ ಪೊಟ್ಯಾಷ್ ಹಾಕಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Coconut_on_tree.jpg/640px-Coconut_on_tree.jpg", "🥥"),
    FarmingTip("Areca Nut", "Areca Disease Alert", "ಅಡಿಕೆ ರೋಗ ಎಚ್ಚರಿಕೆ",
        "Watch for yellow leaves on areca nut — this signals root rot. Improve drainage immediately.",
        "ಅಡಿಕೆ ಎಲೆ ಹಳದಿಯಾದರೆ ಬೇರು ಕೊಳೆ ಸಂಕೇತ. ತಕ್ಷಣ ನೀರು ಹರಿಯುವಂತೆ ಮಾಡಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Arecanut_tree.jpg/640px-Arecanut_tree.jpg", "🌴"),
    FarmingTip("Areca Nut", "Koleroga Prevention", "ಕೊಳೆರೋಗ ತಡೆ",
        "Spray 1% Bordeaux mixture before monsoon to prevent Koleroga disease in areca nut.",
        "ಅಡಿಕೆ ಕೊಳೆರೋಗ ತಡೆಯಲು ಮಳೆಗಾಲ ಮೊದಲು 1% ಬೋರ್ಡೋ ದ್ರಾವಣ ಸಿಂಪಡಿಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Arecanut_tree.jpg/640px-Arecanut_tree.jpg", "🌴"),
    FarmingTip("Tur Dal", "Pod Borer Control", "ತೊಗರಿ ಕೀಟ ನಿಯಂತ್ರಣ",
        "Spray Chlorpyrifos 2ml/litre to control pod borer in Tur Dal. Best time: evening spray.",
        "ತೊಗರಿ ಕಾಯಿ ಕೊರಕ ನಿಯಂತ್ರಣಕ್ಕೆ 2ಮಿಲಿ ಕ್ಲೋರ್‌ಪೈರಿಫಾಸ್ ಪ್ರತಿ ಲೀಟರ್ ನೀರಿಗೆ ಬೆರೆಸಿ ಸಂಜೆ ಸಿಂಪಡಿಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Pigeonpea_flowers.jpg/640px-Pigeonpea_flowers.jpg", "🌿"),
    FarmingTip("Tur Dal", "Sowing Time – Kalaburagi", "ತೊಗರಿ ಬಿತ್ತನೆ ಸಮಯ",
        "In Kalaburagi, sow Tur Dal between June 15 – July 15. Use ICPL-87 variety.",
        "ಕಲಬುರಗಿ ಜಿಲ್ಲೆಯಲ್ಲಿ ಜೂನ್ 15 – ಜುಲೈ 15 ರ ನಡುವೆ ತೊಗರಿ ಬಿತ್ತಿ. ICPL-87 ತಳಿ ಬಳಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Pigeonpea_flowers.jpg/640px-Pigeonpea_flowers.jpg", "🌿"),
    FarmingTip("Tur Dal", "Wilt Disease Control", "ತೊಗರಿ ಬಾಡು ರೋಗ",
        "If plants wilt suddenly, remove infected plants immediately. Use wilt-resistant varieties next season.",
        "ಗಿಡ ಇದ್ದಕ್ಕಿದ್ದಂತೆ ಬಾಡಿದರೆ ತಕ್ಷಣ ತೆಗೆಯಿರಿ. ಮುಂದಿನ ಬೆಳೆಗೆ ರೋಗ ನಿರೋಧಕ ತಳಿ ಬಳಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Pigeonpea_flowers.jpg/640px-Pigeonpea_flowers.jpg", "🌿"),
    FarmingTip("Cotton", "Bollworm Trap", "ಹತ್ತಿ ಬೊಂಡ ಹುಳು ಎಚ್ಚರಿಕೆ",
        "Set up pheromone traps (5 per acre) to monitor bollworm in cotton. Replace every 3 weeks.",
        "ಹತ್ತಿ ಬೊಂಡ ಹುಳು ಮೇಲ್ವಿಚಾರಣೆಗೆ ಪ್ರತಿ ಎಕರೆಗೆ 5 ಫೆರೋಮೋನ್ ಬಲೆ ಇಡಿ. ಪ್ರತಿ 3 ವಾರಕ್ಕೆ ಬದಲಿಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/CottonPlant.JPG/640px-CottonPlant.JPG", "🌸"),
    FarmingTip("Cotton", "Sedam Black Soil Fertilizer", "ಹತ್ತಿ ಗೊಬ್ಬರ – ಸೇಡಂ",
        "In Sedam black soil, apply 50kg DAP + 25kg MOP per acre before sowing. Top-dress urea at 30 days.",
        "ಸೇಡಂ ಕಪ್ಪು ಮಣ್ಣಿನಲ್ಲಿ ಬಿತ್ತನೆ ಮೊದಲು ಪ್ರತಿ ಎಕರೆಗೆ 50ಕೆಜಿ DAP + 25ಕೆಜಿ MOP ಹಾಕಿ. 30 ದಿನದ ನಂತರ ಯೂರಿಯಾ ನೀಡಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/CottonPlant.JPG/640px-CottonPlant.JPG", "🌸"),
    FarmingTip("Cotton", "Pink Bollworm Spray", "ಗುಲಾಬಿ ಬೊಂಡ ಹುಳು ನಿಯಂತ್ರಣ",
        "Spray Spinosad 1ml/litre to control pink bollworm. Do not spray during flowering.",
        "ಗುಲಾಬಿ ಬೊಂಡ ಹುಳು ನಿಯಂತ್ರಣಕ್ಕೆ 1ಮಿಲಿ ಸ್ಪಿನೋಸ್ಯಾಡ್ ಪ್ರತಿ ಲೀಟರ್‌ಗೆ ಸಿಂಪಡಿಸಿ. ಹೂ ಬಿಡುವ ಸಮಯ ಸಿಂಪಡಿಸಬೇಡಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/CottonPlant.JPG/640px-CottonPlant.JPG", "🌸"),
    FarmingTip("Jowar", "Jowar Sowing – Kalaburagi", "ಜೋಳ ಬಿತ್ತನೆ – ಕಲಬುರಗಿ",
        "Sow Rabi Jowar in Kalaburagi between Oct 1–15. Use SPH-1616 or M-35-1 for black soil.",
        "ಕಲಬುರಗಿಯಲ್ಲಿ ರಬಿ ಜೋಳ ಅಕ್ಟೋಬರ್ 1-15 ರ ನಡುವೆ ಬಿತ್ತಿ. ಕಪ್ಪು ಮಣ್ಣಿಗೆ SPH-1616 ಅಥವಾ M-35-1 ತಳಿ ಬಳಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Sorghum_bicolor_-_K%C3%B6hler%E2%80%93s_Medizinal-Pflanzen-262.jpg/480px-Sorghum_bicolor_-_K%C3%B6hler%E2%80%93s_Medizinal-Pflanzen-262.jpg", "🌾"),
    FarmingTip("Jowar", "Shoot Fly Prevention", "ಜೋಳ ಚಿಗುರು ನೊಣ ತಡೆ",
        "Treat Jowar seeds with Imidacloprid 70WS (5g/kg seed) before sowing to prevent shoot fly.",
        "ಚಿಗುರು ನೊಣ ತಡೆಯಲು ಬಿತ್ತನೆ ಮೊದಲು ಪ್ರತಿ ಕೆಜಿ ಬೀಜಕ್ಕೆ 5ಗ್ರಾಂ ಇಮಿಡಾಕ್ಲೋಪ್ರಿಡ್ 70WS ಬಳಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Sorghum_bicolor_-_K%C3%B6hler%E2%80%93s_Medizinal-Pflanzen-262.jpg/480px-Sorghum_bicolor_-_K%C3%B6hler%E2%80%93s_Medizinal-Pflanzen-262.jpg", "🌾"),
    FarmingTip("Sunflower", "Head Rot Prevention", "ಸೂರ್ಯಕಾಂತಿ ತಲೆ ಕೊಳೆ ತಡೆ",
        "Spray Mancozeb 2.5g/litre to prevent head rot. Avoid overhead irrigation after flowering.",
        "ಸೂರ್ಯಕಾಂತಿ ತಲೆ ಕೊಳೆ ತಡೆಯಲು 2.5ಗ್ರಾಂ ಮ್ಯಾಂಕೋಝೆಬ್ ಪ್ರತಿ ಲೀಟರ್‌ಗೆ ಸಿಂಪಡಿಸಿ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Sunflower_sky_backdrop.jpg/640px-Sunflower_sky_backdrop.jpg", "🌻"),
    FarmingTip("Sunflower", "Bee Pollination Tip", "ಸೂರ್ಯಕಾಂತಿ ಜೇನು ಪರಾಗಸ್ಪರ್ಶ",
        "Place 2 beehives per acre near sunflower to improve pollination and increase yield by 20%.",
        "ಸೂರ್ಯಕಾಂತಿ ಹೊಲದ ಬಳಿ ಪ್ರತಿ ಎಕರೆಗೆ 2 ಜೇನು ಗೂಡು ಇಡಿ. ಇದರಿಂದ 20% ಹೆಚ್ಚು ಇಳುವರಿ ಸಿಗುತ್ತದೆ.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Sunflower_sky_backdrop.jpg/640px-Sunflower_sky_backdrop.jpg", "🌻")
)

// ─── SUCCESS STORIES ────────────────────────────────────────
val stories = listOf(
    SuccessStory("Ramesh", "Mandya District", "Paddy",
        "Ramesh was losing 30% of his paddy crop to pests every year.",
        "ರಮೇಶ್ ಪ್ರತಿ ವರ್ಷ 30% ಭತ್ತ ಕೀಟಗಳಿಂದ ಕಳೆದುಕೊಳ್ಳುತ್ತಿದ್ದರು.",
        "After neem oil spray from Raitha-Varta, he saved his crop and earned ₹45,000 extra!",
        "ರೈತ-ವಾರ್ತಾ ಸಲಹೆಯಂತೆ ಬೇವಿನ ಎಣ್ಣೆ ಸಿಂಪಡಿಸಿ ₹45,000 ಹೆಚ್ಚು ಗಳಿಸಿದರು!", "🌾"),
    SuccessStory("Lakshmi", "Hassan District", "Tomato",
        "Lakshmi's tomatoes were getting blight disease every season.",
        "ಲಕ್ಷ್ಮಿ ಅವರ ಟೊಮೆಟೊಗೆ ಪ್ರತಿ ಮಳೆಗಾಲ ರೋಗ ಬರುತ್ತಿತ್ತು.",
        "Bordeaux mixture tip gave her zero disease loss. Income doubled to ₹80,000!",
        "ಬೋರ್ಡೋ ಮಿಶ್ರಣ ಸಲಹೆ ಅನುಸರಿಸಿ ₹80,000 ಆದಾಯ ದ್ವಿಗುಣ ಮಾಡಿದರು!", "🍅"),
    SuccessStory("Suresh", "Shivamogga District", "Areca Nut",
        "Suresh lost half his areca nut yield to Koleroga disease for 3 years.",
        "ಸುರೇಶ್ 3 ವರ್ಷ ಕೊಳೆರೋಗದಿಂದ ಅರ್ಧ ಅಡಿಕೆ ಕಳೆದುಕೊಂಡಿದ್ದರು.",
        "Pre-monsoon Bordeaux spray saved his entire crop. He earned ₹1.2 lakh more!",
        "ಮಳೆಗಾಲ ಮೊದಲು ಸಿಂಪಡಿಸಿ ₹1.2 ಲಕ್ಷ ಹೆಚ್ಚು ಗಳಿಸಿದರು!", "🌴"),
    SuccessStory("Basavaraj", "Sedam, Kalaburagi", "Tur Dal",
        "Basavaraj lost his Tur Dal crop to pod borer every year for 4 seasons in Sedam.",
        "ಸೇಡಂನ ಬಸವರಾಜ 4 ವರ್ಷ ತೊಗರಿ ಕಾಯಿ ಕೊರಕದಿಂದ ಬೆಳೆ ಕಳೆದುಕೊಳ್ಳುತ್ತಿದ್ದರು.",
        "After evening Chlorpyrifos spray from Raitha-Varta, his 5-acre yield doubled. Earned ₹90,000 extra!",
        "ರೈತ-ವಾರ್ತಾ ಸಲಹೆಯಂತೆ ಕ್ಲೋರ್‌ಪೈರಿಫಾಸ್ ಸಿಂಪಡಿಸಿ 5 ಎಕರೆ ಬೆಳೆ ದ್ವಿಗುಣ. ₹90,000 ಹೆಚ್ಚು ಗಳಿಸಿದರು!", "🌿"),
    SuccessStory("Yellamma", "Sedam Taluk", "Cotton",
        "Yellamma's cotton field in Sedam's black soil was hit by pink bollworm 3 years in a row.",
        "ಸೇಡಂ ಕಪ್ಪು ಮಣ್ಣಿನ ಯಲ್ಲಮ್ಮ ಅವರ ಹತ್ತಿಗೆ 3 ವರ್ಷ ಗುಲಾಬಿ ಬೊಂಡ ಹುಳು ಬಾಧೆ ಆಗಿತ್ತು.",
        "Pheromone traps + Spinosad spray saved her full 8-acre crop. Income up by ₹1.1 lakh!",
        "ಫೆರೋಮೋನ್ ಬಲೆ + ಸ್ಪಿನೋಸ್ಯಾಡ್ ಸಿಂಪಡಣೆಯಿಂದ 8 ಎಕರೆ ಬೆಳೆ ಉಳಿಸಿ ₹1.1 ಲಕ್ಷ ಹೆಚ್ಚು ಗಳಿಸಿದರು!", "🌸"),
    SuccessStory("Mallappa", "Kalaburagi District", "Jowar",
        "Mallappa's Rabi Jowar was getting destroyed by shoot fly every October in Kalaburagi.",
        "ಕಲಬುರಗಿಯ ಮಲ್ಲಪ್ಪ ಅವರ ರಬಿ ಜೋಳ ಪ್ರತಿ ಅಕ್ಟೋಬರ್‌ನಲ್ಲಿ ಚಿಗುರು ನೊಣದಿಂದ ನಾಶವಾಗುತ್ತಿತ್ತು.",
        "Imidacloprid seed treatment before sowing completely eliminated shoot fly. Yield up 40%!",
        "ಬಿತ್ತನೆ ಮೊದಲು ಇಮಿಡಾಕ್ಲೋಪ್ರಿಡ್ ಬೀಜೋಪಚಾರದಿಂದ ಚಿಗುರು ನೊಣ ನಿಯಂತ್ರಣ. ಇಳುವರಿ 40% ಹೆಚ್ಚಾಯಿತು!", "🌾")
)

// ─── MAIN ACTIVITY ──────────────────────────────────────────
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RaithaVartaTheme {
                RaithaVartaApp()
            }
        }
    }
}

// ─── CROP CHIP — top-level composable ───────────────────────
@Composable
fun CropChip(label: String) {
    Surface(color = Color(0xFF1B5E20), shape = RoundedCornerShape(50)) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            fontSize = 12.sp, color = Color.White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RaithaVartaApp() {
    val context = LocalContext.current
    var isKannada by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCrop by remember { mutableStateOf("All") }
    var showExpertDialog by remember { mutableStateOf(false) }

    val crops = listOf("All", "Paddy", "Tomato", "Coconut", "Areca Nut",
        "Tur Dal", "Cotton", "Jowar", "Sunflower")
    val filteredTips = if (selectedCrop == "All") tips else tips.filter { it.crop == selectedCrop }
    val tipPagerState   = rememberPagerState(pageCount = { filteredTips.size })
    val storyPagerState = rememberPagerState(pageCount = { stories.size })

    if (showExpertDialog) {
        Dialog(onDismissRequest = { showExpertDialog = false }) {
            Card(shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✅", fontSize = 48.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (isKannada) "ನಿಮ್ಮ ಪ್ರಶ್ನೆ ತಜ್ಞರಿಗೆ ಕಳುಹಿಸಲಾಗಿದೆ!"
                        else "Query sent to our agricultural expert!",
                        fontWeight = FontWeight.Bold, fontSize = 16.sp,
                        textAlign = TextAlign.Center, color = Color(0xFF1B5E20))
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (isKannada) "24 ಗಂಟೆಯಲ್ಲಿ ಉತ್ತರ ನಿರೀಕ್ಷಿಸಿ."
                        else "Expect a response within 24 hours.",
                        fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { showExpertDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))) {
                        Text(if (isKannada) "ಸರಿ" else "OK", color = Color.White)
                    }
                }
            }
        }
    }

    Scaffold(containerColor = Color(0xFFF1F8E9)) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            // ── TOP BAR
            Row(modifier = Modifier.fillMaxWidth()
                .background(Color(0xFF2E7D32))
                .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(if (isKannada) "ರೈತ-ವಾರ್ತಾ 🌾" else "Raitha-Varta 🌾",
                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Button(onClick = { isKannada = !isKannada },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))) {
                    Text(if (isKannada) "EN" else "ಕನ್ನಡ", color = Color.White)
                }
            }

            // ── MAIN TABS
            TabRow(selectedTabIndex = selectedTab,
                containerColor = Color(0xFF388E3C), contentColor = Color.White) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 },
                    selectedContentColor = Color.White, unselectedContentColor = Color(0xFFB0BEC5),
                    text = { Text(if (isKannada) "ಸಲಹೆ" else "Daily Tips", fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 },
                    selectedContentColor = Color.White, unselectedContentColor = Color(0xFFB0BEC5),
                    text = { Text(if (isKannada) "ಯಶಸ್ಸು" else "Stories", fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 },
                    selectedContentColor = Color.White, unselectedContentColor = Color(0xFFB0BEC5),
                    text = { Text(if (isKannada) "ಬಗ್ಗೆ" else "About", fontWeight = FontWeight.Bold) })
            }

            // ── TIPS TAB
            if (selectedTab == 0) {
                ScrollableTabRow(selectedTabIndex = crops.indexOf(selectedCrop),
                    containerColor = Color.Transparent, edgePadding = 8.dp) {
                    crops.forEach { crop ->
                        Tab(
                            selected = selectedCrop == crop,
                            onClick = { selectedCrop = crop },
                            selectedContentColor = Color(0xFF2E7D32),
                            unselectedContentColor = Color.Gray,
                            text = {
                                Text(crop,
                                    fontWeight = if (selectedCrop == crop) FontWeight.Bold else FontWeight.Normal)
                            }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                HorizontalPager(state = tipPagerState,
                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                    pageSpacing = 12.dp) { page ->
                    TipCard(tip = filteredTips[page], isKannada = isKannada,
                        onExpertClick = { showExpertDialog = true })
                }
                DotIndicators(size = filteredTips.size, current = tipPagerState.currentPage)
                Spacer(Modifier.height(8.dp))
            }

            // ── STORIES TAB
            if (selectedTab == 1) {
                Spacer(Modifier.height(8.dp))
                HorizontalPager(state = storyPagerState,
                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                    pageSpacing = 12.dp) { page ->
                    StoryCard(story = stories[page], isKannada = isKannada)
                }
                DotIndicators(size = stories.size, current = storyPagerState.currentPage)
                Spacer(Modifier.height(8.dp))
            }

            // ── ABOUT TAB
            if (selectedTab == 2) {
                Column(modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Spacer(Modifier.height(20.dp))

                    // Logo
                    Box(modifier = Modifier.size(90.dp).clip(CircleShape)
                        .background(Color(0xFF2E7D32)),
                        contentAlignment = Alignment.Center) {
                        Text("🌾", fontSize = 44.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Raitha-Varta", fontWeight = FontWeight.Bold,
                        fontSize = 26.sp, color = Color(0xFF1B5E20))
                    Text("Agriculture Flash-Card Advisor", fontSize = 13.sp, color = Color.Gray)

                    Spacer(Modifier.height(24.dp))

                    // Karnataka Overview Card
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
                        elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text("📍 Karnataka Agricultural Overview",
                                fontSize = 11.sp, color = Color(0xFFA5D6A7), fontWeight = FontWeight.Medium)
                            Text("Karnataka, India",
                                fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            Spacer(Modifier.height(10.dp))
                            Text("Major Crops", fontSize = 12.sp, color = Color(0xFFA5D6A7), fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("🌾 Paddy", "🌾 Jowar", "🌿 Ragi").forEach { CropChip(it) }
                            }
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("🌿 Tur Dal", "🫘 Chickpea", "🌿 Green Gram").forEach { CropChip(it) }
                            }
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("🌸 Cotton", "🌻 Sunflower", "🌰 Groundnut").forEach { CropChip(it) }
                            }
                            Spacer(Modifier.height(12.dp))
                            Text("Agro Profile", fontSize = 12.sp, color = Color(0xFFA5D6A7), fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(6.dp))
                            Text("Karnataka features diverse agro-climatic zones — semi-arid, dryland, and coastal. Key soil types include black cotton soil, red soil, and laterite soil.",
                                fontSize = 12.sp, color = Color(0xFFCCCCCC), lineHeight = 18.sp)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // App Details Card
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(3.dp)) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text("App Details", fontWeight = FontWeight.Bold,
                                fontSize = 15.sp, color = Color(0xFF1B5E20))
                            Spacer(Modifier.height(10.dp))
                            InfoRow("📱", "Version", "1.0")
                            InfoRow("🌿", "Crops Covered", "Paddy, Tomato, Coconut, Areca Nut, Tur Dal, Cotton, Jowar, Sunflower")
                            InfoRow("🗣️", "Languages", "English & ಕನ್ನಡ")
                            InfoRow("🔬", "Data Source", "Krishi Vigyana Kendra (KVK), Karnataka")
                            InfoRow("🤖", "Built With", "Android Jetpack Compose + GenAI")
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Developer Card
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20)),
                        elevation = CardDefaults.cardElevation(6.dp)) {
                        Column(modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("👨‍💻", fontSize = 36.sp)
                            Spacer(Modifier.height(6.dp))
                            Text("Developed By", fontSize = 11.sp, color = Color(0xFFA5D6A7))
                            Text("Raghavendra Tuller",
                                fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
                            Spacer(Modifier.height(4.dp))
                            Text("Android Developer", fontSize = 13.sp, color = Color(0xFFCCCCCC))
                            Spacer(Modifier.height(2.dp))
                            Text("raghavendratuller@gmail.com", fontSize = 12.sp, color = Color(0xFFA5D6A7))
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = {
                                context.startActivity(Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.linkedin.com/in/raghavendra-tuller/")))
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B5)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()) {
                                Text("🔗  Connect on LinkedIn",
                                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = {
                                context.startActivity(Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:raghavendratuller@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "Raitha-Varta App")
                                })
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()) {
                                Text("✉️  Send Email",
                                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Mission Card
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text("🎯 Our Mission", fontWeight = FontWeight.Bold,
                                fontSize = 15.sp, color = Color(0xFF1B5E20))
                            Spacer(Modifier.height(6.dp))
                            Text("Bringing expert agricultural knowledge from Krishi Vigyana Kendra " +
                                    "to every farmer's fingertips — in their own language, one actionable tip at a time.",
                                fontSize = 13.sp, lineHeight = 21.sp, color = Color(0xFF424242))
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    Text("Made with ❤️ for Karnataka Farmers", fontSize = 13.sp, color = Color.Gray)
                    Text("© 2025 Raitha-Varta", fontSize = 12.sp, color = Color(0xFFBDBDBD))
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

// ─── REUSABLE COMPOSABLES ───────────────────────────────────
@Composable
fun InfoRow(icon: String, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top) {
        Text(icon, fontSize = 15.sp, modifier = Modifier.width(28.dp))
        Column {
            Text(label, fontSize = 10.sp, color = Color.Gray)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF212121))
        }
    }
}

@Composable
fun DotIndicators(size: Int, current: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.Center) {
        repeat(size) { index ->
            Box(modifier = Modifier.padding(3.dp)
                .size(if (current == index) 10.dp else 7.dp)
                .clip(CircleShape)
                .background(if (current == index) Color(0xFF2E7D32) else Color(0xFFB0BEC5)))
        }
    }
}

// ─── TIP CARD ───────────────────────────────────────────────
@Composable
fun TipCard(tip: FarmingTip, isKannada: Boolean, onExpertClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(model = tip.imageUrl, contentDescription = tip.crop,
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop)
            Column(modifier = Modifier.padding(20.dp)) {
                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(20.dp)) {
                    Text("${tip.emoji} ${tip.crop}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(Modifier.height(10.dp))
                Text(if (isKannada) tip.titleKn else tip.titleEn,
                    fontWeight = FontWeight.Bold, fontSize = 19.sp, color = Color(0xFF1B5E20))
                Spacer(Modifier.height(8.dp))
                Text(if (isKannada) tip.tipKn else tip.tipEn,
                    fontSize = 15.sp, lineHeight = 23.sp, color = Color(0xFF424242))
                Spacer(Modifier.height(16.dp))
                Button(onClick = onExpertClick, modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))) {
                    Text(if (isKannada) "📷 ತಜ್ಞರನ್ನು ಕೇಳಿ" else "📷 Ask an Expert",
                        fontSize = 15.sp, color = Color.White)
                }
            }
        }
    }
}

// ─── STORY CARD ─────────────────────────────────────────────
@Composable
fun StoryCard(story: SuccessStory, isKannada: Boolean) {
    Card(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(story.emoji, fontSize = 60.sp)
            Spacer(Modifier.height(10.dp))
            Text(story.farmerName, fontWeight = FontWeight.Bold,
                fontSize = 22.sp, color = Color(0xFF1B5E20))
            Text("${story.village} • ${story.crop}", fontSize = 13.sp, color = Color.Gray)
            Spacer(Modifier.height(14.dp))
            Surface(color = Color(0xFFFFF9C4), shape = RoundedCornerShape(12.dp)) {
                Text(if (isKannada) story.storyKn else story.storyEn,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 15.sp, lineHeight = 23.sp, color = Color(0xFF424242))
            }
            Spacer(Modifier.height(12.dp))
            Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp)) {
                Text("🏆 " + if (isKannada) story.resultKn else story.resultEn,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 15.sp, fontWeight = FontWeight.Bold,
                    lineHeight = 23.sp, color = Color(0xFF2E7D32))
            }
        }
    }
}
