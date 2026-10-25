# Kasaba Havası

Kotlin ve Jetpack Compose ile, çiftlik oyunlarından esinlenen özgün piksel temalı şehir arama ve hava tahmini uygulaması. Eğitim referansı: [weather-app](https://github.com/tc-yusufburakdemir/weather-app), `main` / `884c9bf`.

## Özellikler

- 81 ilin plaka koduyla arama (`34`, `6`, `06`); geçersiz kodlar için boş sonuç.
- Gerçek hava verisine göre Bugün kasabanda günlüğü ve Çiftlik hazırlığı önerileri.
- Yazarken 350 ms beklemeli arama; klavyeden Ara ve temizleme düğmesi.
- Dünya şehirleri veya yalnızca Türkiye illeri; Türkiye filtresi ilçe sonuçlarını dışlar.
- Arama temizlenince plaka rozetli 10 sabit şehir; seçili şehir vurgusu ve Logcat kaydı.
- SavedStateHandle ile arama, seçim ve filtre durumunu koruma.
- Loading / Success / Empty / Error, shimmer ve tekrar deneme.
- Şehre dokununca tahmin ekranı, geri dönüş ve üst uygulama çubuğu.
- Anlık sıcaklık, hissedilen, nem, rüzgâr; saatlik ve 7 günlük tahmin; Türkçe hava durumu açıklamaları.
- Sistemle değişen açık/koyu tema ve Compose önizlemeleri.
- Retrofit + Gson, Repository, StateFlow, Coroutines ve Navigation Compose.
- API, ViewModel ve hava kodu testleri; R8 ve isteğe bağlı release imzası.

API anahtarı gerekmez. Şehir ve hava verileri Open-Meteo üzerinden alınır.

## Çalıştırma

Android SDK konumunu `local.properties` içine `sdk.dir=...` olarak yazın. JDK 17 veya Android Studio JBR kullanın.

```powershell
./gradlew.bat assembleDebug
./gradlew.bat testDebugUnitTest lintDebug
./gradlew.bat spotlessApply spotlessCheck
./gradlew.bat assembleRelease
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.
Release için `keystore.properties.example` örneğini kullanın; dosya yoksa eğitim amaçlı debug anahtarıyla imzalanır.

## Branch ve remote bilgileri

Çalışma branch'i: `calisma/uygulama`.

- `origin`: `https://github.com/tc-yusufburakdemir/weather-app.git` — eğitim referansı.
- `zehra`: `https://github.com/Zehra-Ustaoglu/Turkcell-Hava.app.git` — katılımcı reposu.

Referans checkpoint'leri: `cp1-bitis` sabit liste, `cp2-bitis` state/arama, `cp3-bitis` API, `cp4-bitis` tahmin/navigasyon. Aktif çalışmayı referans checkpoint branch'leriyle karıştırmayın.

Ayrıntılı başlangıç eksikleri ve yapılan değişiklikler: [docs/IMPLEMENTATION-AUDIT.md](docs/IMPLEMENTATION-AUDIT.md).
Mimari: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).