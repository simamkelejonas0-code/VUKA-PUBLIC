# VUKA AROS - Meta AI Hackathon 2026

**VUKA = Wake Up (Xhosa) | AROS = Eyes | Named after my daughter NINO (5 years)**

AR eyes fusion for rural craft sellers - not a chatbot. Sees object, speaks Xhosa/English, prices it.

### Live Proof
- **1000 requests, 175.8k in, 1M out, $140.59 left from $150** - Meta Dashboard
- **130 builds in 2 days, 57 builds in 13 hours** - Oct 29-31 proof
- **21K APK, dex 26K** - not 8.5K empty
- Camera: `TextureView + Camera2 + 23K base64 + TAP OBJECT -> code 200`

### Build Chain - Termux Only
**Used:** `javac --release 8` + `d8 --min-api 21` + `aapt2 compile/link` + `zipalign` + `apksigner` + `android.jar android-34`
**NOT Used:** Android Studio, Gradle, npm, React, IDE - No laptop

**Model:** `muse-spark-1.3` - 1 model, 1 mouth. Fused from 12 nerves to save credits.

### Files
- `ModelCaller.java` - calls `api.meta.ai/v1/responses` with image
- `VideoLoop.java` - live frame loop
- `MainActivity.java` - no anonymous inner classes - `VukaClick`, `VukaVisionTask` separate

### Challenges Solved
1. `new byte;` -> `new byte;` - 30 builds failed
2. `MainActivity$1` D8 fail on Termux - extracted to separate classes
3. INTERNET perm missing - curl 200 OK but APK blank
4. No logcat - show error in bubble `API code 401`
5. 8.5K vs 21K - learned dex inclusion

### What VUKA Meant To Be
Eyes for people who can't read. Kirkwood youth weave stools, sell clothes. VUKA sees, speaks, prices, posts to Marketplace. Sundays River Valley 6120 to global gallery.

### Repos
- KIRKWOOD-EMERGENCY-FULL - Police/Ambulance/Armed - GPS Locked
- NINO-KIRKWOOD-OS-PROOF - 10 APKs 70MB - 2 Months From Zero
- SEEN-19-FIXED - AR eyes fusion proof

**GitHub:** github.com/simamkelejonas0-code/VUKA-AROS  
**Built by:** Siimamkele Ngxonono - Kirkwood 6120 - Termux Builder
