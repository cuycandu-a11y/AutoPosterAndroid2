package com.gotrend.autoposter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

private val Navy = Color(0xFF101B32)
private val Blue = Color(0xFF3478F6)
private val platforms = listOf("TikTok", "Facebook", "Pinterest", "Instagram", "YouTube", "Shopee Video", "Threads")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = lightColorScheme(primary = Blue)) { AutoPosterScreen() } }
    }
}

@Composable
fun AutoPosterScreen() {
    val context = LocalContext.current
    var mediaUri by remember { mutableStateOf<Uri?>(null) }
    var mediaType by remember { mutableStateOf("video/*") }
    var caption by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Belum ada konten dipilih") }
    val selected = remember { mutableStateListOf(*platforms.toTypedArray()) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        mediaUri = uri
        if (uri != null) {
            val resolved = context.contentResolver.getType(uri) ?: "video/*"
            mediaType = if (resolved.startsWith("image/")) "image/*" else "video/*"
            status = "${if (mediaType == "image/*") "Foto" else "Video"} siap dibagikan"
        } else status = "Pemilihan media dibatalkan"
    }

    Column(
        Modifier.fillMaxSize().background(Color(0xFFF4F6FA))
            .verticalScroll(rememberScrollState()).padding(18.dp)
    ) {
        Text("AUTO POSTER", color = Navy, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text("7 platform • satu tempat mengelola konten", color = Color.Gray)
        Spacer(Modifier.height(18.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Navy), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Text("SOCIAL MEDIA STUDIO", color = Color(0xFFB8CCF5), style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                Text("Satu konten. Tujuh tujuan.", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Siapkan foto/video, caption, lalu bagikan melalui aplikasi sosial pilihanmu.", color = Color(0xFFD7DFEF))
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("1. Pilih foto atau video", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = { picker.launch(arrayOf("image/*", "video/*")) }, modifier = Modifier.fillMaxWidth()) {
            Text(if (mediaUri == null) "Pilih foto / video dari HP" else "Ganti foto / video")
        }
        Text(status, color = Color.Gray, style = MaterialTheme.typography.bodySmall)

        Spacer(Modifier.height(18.dp))
        Text("2. Caption dan hashtag", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = caption, onValueChange = { caption = it }, modifier = Modifier.fillMaxWidth(),
            minLines = 4, placeholder = { Text("Tulis caption dan hashtag di sini...") }
        )
        Spacer(Modifier.height(6.dp))
        OutlinedButton(onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Caption Auto Poster", caption))
            status = "Caption disalin ke clipboard"
        }, enabled = caption.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Salin caption") }

        Spacer(Modifier.height(18.dp))
        Text("3. Pilih platform tujuan", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Text("Pilih platform yang ingin kamu tuju.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        platforms.forEach { platform ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = platform in selected, onCheckedChange = { checked ->
                    if (checked && platform !in selected) selected.add(platform)
                    if (!checked) selected.remove(platform)
                })
                Text(platform, fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                Text(if (platform == "Facebook" || platform == "Instagram" || platform == "Threads") "Meta" else if (platform == "Shopee Video") "Manual" else "Bagikan", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
            }
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            val uri = mediaUri
            if (uri == null) {
                status = "Pilih foto atau video terlebih dahulu"
            } else if (selected.isEmpty()) {
                status = "Pilih minimal satu platform"
            } else {
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = mediaType
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_TEXT, caption)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(send, "Bagikan ke: ${selected.joinToString()}"))
                status = "Menu berbagi dibuka. Pilih aplikasi tujuan dan selesaikan posting di aplikasi tersebut."
            }
        }, modifier = Modifier.fillMaxWidth(), enabled = selected.isNotEmpty()) {
            Text("Bagikan konten")
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = { status = "Pengingat jadwal akan ditambahkan pada pembaruan berikutnya." }, modifier = Modifier.fillMaxWidth()) {
            Text("Jadwalkan posting (segera hadir)")
        }
        Spacer(Modifier.height(18.dp))
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEF9)), shape = RoundedCornerShape(14.dp)) {
            Text(
                "PENTING: Versi ini adalah MVP semi-otomatis. Tombol Bagikan membuka menu berbagi Android; kamu tetap memilih aplikasi dan menekan tombol posting. Auto-publish memerlukan API resmi, izin developer/OAuth, dan dukungan masing-masing platform. Facebook profil pribadi dan Shopee Video disiapkan melalui berbagi manual.",
                modifier = Modifier.padding(14.dp), color = Navy, style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}
