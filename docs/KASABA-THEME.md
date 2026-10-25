# Kasaba Havası teması

Stardew Valley sevgisinden yola çıkan, uygulamaya özel çizilmiş çiftlik ve piksel hava ikonları. Görseller Compose Canvas ile çizilir; harici görsel dosyası veya görsel indirme isteği kullanılmaz.

- Krem kâğıt, yaprak yeşili, bal ve ahşap renkleri; köşeleri kesik panolar ve çerçeveli şehir kartları.
- Okunabilir gövde yazısı ve oyun panosu hissi veren monospace başlıklar.
- Ana ekranda çiftlik; tahmin ekranında hava koduna göre yağmur/kar ayrıntıları.
- Sistem koyu modunda yıldızlar, ay ve ışıklı çiftlik pencereleri.
- Piksel hava ikonları, filiz animasyonuyla yükleme, temaya uygun hata mesajı.
- Çiftlik evi biçiminde yeni uygulama simgesi ve Kasaba Havası adı.
- Dar yüksekliklerde manzara gizlenerek arama sonuçlarına alan bırakılır.

Arama, Türkiye filtresi, seçim, navigasyon ve tahmin API davranışları korunmuştur.

Gündüz ve gece görünümü çalışan Pixel 6 emülatöründe incelendi; şehirden tahmin ekranına geçiş denendi. Tema için iş mantığı testi eklenmedi; mevcut 37 test, APK derlemesi ve lint ile regresyon kontrolü yapıldı.
## Kasaba günlüğü ve plaka araması

Ana sayfadaki Bugün kasabanda ve Çiftlik hazırlığı, listede seçili şehrin (yoksa ilk sonucun) gerçek tahminine göre güncellenir. Hava kodu, sıcaklık, rüzgâr ve şehrin yerel tarihine ait günlük yağış olasılığı kullanılır. Başka güne ait yağış tahmini bugünün önerisine katılmaz. Veri hatasında tekrar deneme gösterilir; yeni şehir seçildiğinde önceki istek iptal edilir.

Manzara, günlük ve şehir listesi birlikte kaydırılır; arama kutusu üstte kalır. Plaka araması 01–81 aralığını, baştaki sıfırı ve Kocaeli/İzmit, Sakarya/Adapazarı, Hatay/Antakya adlarını destekler. Sayısal arama Türkiye il merkezleriyle sınırlanır; yabancı şehir veya ilçe sonucu gösterilmez. Şehir rozetlerinde de bilinen illerin plaka kodları kullanılır.

Kontroller: 48 birim testi, debug APK ve Android lint başarılı.