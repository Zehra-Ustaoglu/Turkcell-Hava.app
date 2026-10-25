# Gereksinim karşılaştırması — 26 Ekim 2026

## Kaynak ve Git durumu

Karşılaştırma: kullanıcının gönderdiği Uygulama 2, 3 ve 4 ekran görüntüleri ile eğitim reposunun `main` branch'i. GitHub bağlantısından güncel SHA `884c9bf6166dfb36dd133c97879bc1965aa00544` doğrulandı; yerel `origin/main` aynı commit'te.

Başlangıç ve bitiş branch'i `calisma/uygulama`, başlangıç HEAD `72b4b54`. `origin` eğitim reposu, `zehra` katılımcı reposu. Branch değiştirilmedi, remote adresleri değiştirilmedi, commit/push yapılmadı.

Başlangıçta CityApi, CityRepository ve CitySearchResponse dosyalarında kaydedilmemiş Türkiye/il filtresi vardı. İşlem öncesi kaynaklar `build/task-backup/before-feature-completion.zip` içinde saklandı. Filtre kodu ortak City modeline taşındı ve kullanıcıya açılıp kapanabilen seçenek olarak sunuldu. Varsayılan dünya araması referans reponun davranışını korur.

## Ekran görüntülerine göre başlangıç eksikleri

| Gereksinim | Başlangıç durumu | Yapılan işlem |
|---|---|---|
| CityCard, Preview, 10 şehir, plaka rozeti | Kod vardı, aktif API ekranında sabit liste kullanılmıyordu | Arama temizlendiğinde 10 şehir plaka kartlarıyla gösterilir; kartlardan tahmine gidilir |
| Karta basınca `Tıklandı: Ankara` logu | Yoktu | Şehir seçimi sırasında Logcat kaydı eklendi |
| Açık/koyu tema ve uygulama adı | Vardı | Tema ve Hava.app adı korundu |
| TopAppBar | Yoktu | Arama ve tahmin ekranlarına eklendi |
| Yazdıkça filtreleme | Yoktu; yalnızca Ara düğmesiyle istek atılıyordu | 350 ms beklemeli otomatik API araması; önceki istek iptal edilir |
| Döndürmede arama ve seçim | SavedStateHandle vardı | Korundu; filtre durumu da eklendi; geri yüklenen sorgu yeniden aranır |
| Seçili şehir vurgusu | Vardı | Tahmin ekranından dönüşte korunacak şekilde yeni kartlara uyarlandı |
| Sonuç yoksa Şehir bulunamadı | Aynı başlangıç yönergesi gösteriliyordu | Ayrı EmptyView ile düzeltildi |
| Temizleme ve sonuç sayısı | Temizleme fonksiyonu UI'a bağlı değildi; sayı vardı | Temizleme simgesi bağlandı, sayı korundu |
| INTERNET, Retrofit, Gson, API ve Repository | Vardı | Tahmin API'si ve test edilebilir repository arayüzleriyle genişletildi |
| Loading / Success / Empty / Error | Temel yapı vardı | Ortak görünümler, shimmer ve anlaşılır hata mesajları eklendi |
| Tekrar dene | Vardı | Arama ve tahmin ekranlarında korundu |
| Klavyede Ara | Yoktu | ImeAction.Search ve klavye kapatma eklendi |
| OkHttp logu (bonus) | Yoktu | Debug derlemesinde yöntem, sunucu, yol ve HTTP durum kodu loglanır |

`rememberSaveable` yerine SavedStateHandle ve ViewModel kullanılır; ekran döndürme gereksinimini karşılar ve süreç yeniden oluşturulmasına da destek verir. Görsellerdeki eğitim/kitapçık/sayaç yönergeleri uygulama özelliği sayılmadı.

## Referans main sürümüne göre eklenenler

- Navigation Compose ile şehir → tahmin → geri dönüş.
- ForecastApi, WeatherRepository, ForecastResponse ve sütun verilerini saat/gün satırlarına dönüştürme.
- Anlık sıcaklık, hissedilen sıcaklık, nem, rüzgâr, 24 saatlik ve 7 günlük tahmin.
- WMO kodundan Türkçe durum ve emoji; uzun isimler için taşma yönetimi.
- Lifecycle farkındalıklı StateFlow toplama, ortak boş/hata/yükleme görünümleri.
- Referansın API, ViewModel, tahmin dönüşümü ve hava kodu testleri; otomatik arama, temizleme, durum geri yükleme ve Türkiye filtresi için ek testler.
- Release küçültme (R8), Gson kuralları ve isteğe bağlı imza yapılandırması.

## Doğrulama sınırları

Birim testleri API'yi yerel MockWebServer ve sahte repository üzerinden doğrular. Gerçek telefonda döndürme, uçak modu ve canlı Open-Meteo yanıtı ayrıca denenmelidir. Derleme ve test sonuçları son teslim mesajında belirtilir.

## Çalıştırılan kontroller

- `assembleDebug`: başarılı; APK `app/build/outputs/apk/debug/app-debug.apk`.
- `testDebugUnitTest`: 37 test, 0 başarısızlık, 0 hata.
- `spotlessCheck`: başarılı.
- `lintDebug`: başarılı; 16 uyarı yalnızca bağımlılık/eklenti sürüm güncelleme önerileridir.
- `git diff --check`: başarılı.

Release yapılandırması referanstan alındı; `assembleRelease` bu çalışmada çalıştırılmadı. Cihaz/emülatör üzerinde etkileşim testi yapılmadı.