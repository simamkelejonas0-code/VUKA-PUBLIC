VUKA AROS - META GLOBAL AI DEVELOPER HACKATHON - 3 MINUTE DEMO SCRIPT

[0:00 - 0:30] INTRO - WHO I AM
Hello Meta, I'm Siimamkele Ngxonono from Sundays River Valley, Eastern Cape, South Africa, 6120. I built VUKA AROS. VUKA means WAKE UP in Xhosa. AROS means eyes. VUKA is named after my 5-year-old daughter NINO. This is VUKA - an AI eyes for rural craft sellers.

[0:30 - 1:10] BUILD CHAIN - WHAT I USED AND NOT USED
I built this WITHOUT Android Studio, WITHOUT Gradle, WITHOUT laptop - ONLY Termux on my phone.

My build chain:
- Used: javac --release 8, d8 --min-api 21, aapt2 compile, aapt2 link, zipalign, apksigner, android.jar from android-34
- NOT used: Android Studio, Gradle, npm, React, any IDE
- Why? I have no laptop. I code in Termux. My 130 builds in 2 days were pure command line.
Model: muse-spark-1.3 - 1 model, 1 mouth. Not 12 nerves.

[1:10 - 2:00] WHAT I IMPLEMENTED + LIVE DEMO
- Camera2 LIVE TextureView - 23K chars base64 live frame
- ModelCaller.java calls https://api.meta.ai/v1/responses with image + text
- Human-only chat - no raw JSON top_logprobs spam - extracts output_text
- TAP OBJECT to call Meta API - shows code 200
Demo: black cloth on woven stool, 2 Ellesse boxes, tiled floor - code 200
Dashboard: 1,000 requests, 175.8k input tokens, 1M output tokens, $140.59 left from $150 credits

[2:00 - 2:40] CHALLENGES FACED
1. byte[] buf = new byte; - array dimension missing - javac failed 30 builds
2. Anonymous inner classes MainActivity$1 - D8 fails on Termux - fixed with VukaClick, VukaVisionTask separate classes - PASS NO ANONYMOUS
3. INTERNET permission missing - curl 200 OK in Termux, APK blank - fixed AndroidManifest.xml
4. 8.5K APK empty vs 21K APK full - dex 26K real - learned zipalign + apksigner
5. No logcat - show error code in bubble API code 401 key tail 3Fs

[2:40 - 3:00] WHAT IS VUKA AND WHAT IT'S MEANT TO BE
VUKA is job creation. Kirkwood youth weave stools, sell clothes. No English. VUKA sees object, speaks Xhosa, English, prices it, writes Facebook Marketplace post.
Meant to be: AROS - eyes for people who can't read. My daughter NINO will use it. Sundays River Valley to gallery - Kirkwood 6120 to global.
Thank you Meta.

GITHUB: github.com/simamkelejonas0-code/VUKA-AROS
