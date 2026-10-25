package com.kampplus.hava.model

data class FarmAdvice(val story: String, val preparations: List<String>) {
    companion object {
        fun from(forecast: Forecast): FarmAdvice {
            val current = forecast.current
            val today = forecast.daily.firstOrNull { it.date == current.observedAt.toLocalDate() }
            val condition = WeatherCondition.fromWeatherCode(current.weatherCode)
            val wet = condition in listOf(WeatherCondition.DRIZZLE, WeatherCondition.RAIN, WeatherCondition.RAIN_SHOWERS)
            val snowy = condition in listOf(WeatherCondition.SNOW, WeatherCondition.SNOW_SHOWERS)
            val storm = condition == WeatherCondition.THUNDERSTORM
            val rainExpected = (today?.precipitationProbability ?: 0) >= 50
            val story = when {
                storm -> "Kasabanın üstünde gök gürültüsü var. Bugün pencere kenarında bir çiftlik günlüğü yazma günü."
                snowy -> "Kasabaya kar uğramış! Sıcak bir içecek hazırla, beyaza bürünen manzaranın tadını çıkar."
                wet -> "Çatıda yağmurun sesi var. Bir fincan çay ve sevdiğin bir kitap bugün kasabaya çok yakışır."
                condition == WeatherCondition.FOG -> "Kasaba sislerin arasına saklanmış. Bugün yavaş bir sabahın ve sakin planların günü."
                current.temperatureC >= 30 -> "Kasabada sıcacık bir gün. Gölge bir köşe bulup çiftliğin yeni planlarını çizmeye ne dersin?"
                current.temperatureC <= 5 -> "Kasabanın havası epey serin. Bugün sıcacık bir mola ve yumuşacık bir battaniye iyi gider."
                condition in listOf(
                    WeatherCondition.CLEAR,
                    WeatherCondition.MAINLY_CLEAR
                ) -> "Güneş kasabanı selamlıyor. Kısa bir yürüyüşe çıkıp günün küçük güzelliklerini keşfet."
                condition in listOf(
                    WeatherCondition.OVERCAST,
                    WeatherCondition.PARTLY_CLOUDY
                ) -> "Bulutlar kasabanın üzerinde geziniyor. Sevdiğin müziği aç, bugünün küçük işlerini keyifle tamamla."
                else -> "Kasabada yeni bir gün başladı. Gökyüzünü izle, gününü kendi ritminde planla."
            }
            val tips = buildList {
                if (storm) add("Açık hava planlarını ertele; kapalı bir yerde kal.")
                if (snowy) add("Kalın katlarını ve kaymayan ayakkabılarını hazırla.")
                if (wet ||
                    rainExpected
                ) {
                    add(if (wet) "Şemsiyeni ve yağmurluğunu yanına al." else "Bugün yağış ihtimali yüksek; çantana bir şemsiye koy.")
                }
                if ((current.windSpeedKmh ?: 0.0) >= 35) add("Rüzgâr güçlü; balkondaki hafif eşyaları sabitle.")
                if (current.temperatureC <= 10 && !snowy) add("Dışarı çıkarken sıcak tutan bir kat al.")
                if (current.temperatureC >= 28) add("Su şişeni hazırla, dışarıdaki molalarını gölgede ver.")
                if (isEmpty()) {
                    add(
                        if (condition == WeatherCondition.UNKNOWN || condition == WeatherCondition.FOG) {
                            "Dışarı çıkmadan önce güncel tahmini ve görüş durumunu kontrol et."
                        } else {
                            "Hava sakin görünüyor; dışarıdaki küçük işlerini planlayabilirsin."
                        }
                    )
                }
            }
            return FarmAdvice(story, tips)
        }
    }
}
