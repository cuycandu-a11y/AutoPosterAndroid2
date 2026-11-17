# Auto Poster Android — 7 Platform MVP

Aplikasi Android Kotlin + Jetpack Compose untuk menyiapkan foto/video, caption, memilih target platform, menyalin caption, dan membuka Android Sharesheet.

## Target platform
- TikTok
- Facebook (profil pribadi melalui berbagi manual; Facebook Page dapat dikembangkan melalui Graph API dan izin yang sesuai)
- Pinterest
- Instagram
- YouTube
- Shopee Video (berbagi/pengingat manual sampai akses API resmi yang sesuai tersedia)
- Threads

## Fitur MVP saat ini
- Dashboard Android.
- Memilih foto atau video dari HP.
- Menulis caption dan hashtag.
- Memilih satu atau beberapa platform tujuan.
- Menyalin caption ke clipboard.
- Membuka menu berbagi Android untuk meneruskan media ke aplikasi yang tersedia.

## Batasan penting
Versi ini BELUM melakukan auto-publish dan belum menjalankan jadwal posting otomatis. Android Sharesheet hanya membantu meneruskan media; pengguna tetap memilih aplikasi tujuan dan menyelesaikan posting di aplikasi tersebut. Daftar platform terpilih ditampilkan pada judul menu berbagi, tetapi Android tidak menjamin pengiriman ke semua platform sekaligus.

Auto-publish memerlukan integrasi API resmi per platform, OAuth, izin developer, kemungkinan audit/peninjauan, dan backend aman untuk token. TikTok Content Posting API, Meta Graph API/Instagram API, Pinterest API, YouTube Data API, dan Threads API masing-masing memiliki persyaratan. Jangan memasukkan password atau token akun ke aplikasi.

## Build APK hanya dengan HP Android
1. Unduh ZIP proyek dan ekstrak di HP.
2. Login/buat akun di https://github.com melalui Chrome.
3. Buat repository baru bernama `AutoPosterAndroid`.
4. Upload seluruh isi folder proyek hasil ekstrak ke repository, termasuk folder `.github/workflows`.
5. Buka tab **Actions** dan izinkan GitHub Actions jika diminta.
6. Pilih workflow **Build Auto Poster APK**, tekan **Run workflow**.
7. Setelah selesai, buka hasil run dan unduh Artifact `AutoPoster-debug-apk`.
8. Ekstrak artifact, lalu instal `app-debug.apk`. Jika diminta, izinkan Chrome/File Manager memasang aplikasi dari sumber tersebut.

Jika upload melalui browser HP sulit, aktifkan **Situs desktop** di Chrome. Pastikan folder `.github` ikut terunggah.

## Detail teknis
- Package: `com.gotrend.autoposter`
- Min SDK: 26 (Android 8.0)
- Compile/Target SDK: 35
- Java: 17
- Gradle: 8.9
- APK debug untuk pengujian pribadi; belum ditandatangani untuk distribusi Play Store.
