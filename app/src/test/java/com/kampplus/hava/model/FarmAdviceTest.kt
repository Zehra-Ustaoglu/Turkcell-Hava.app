package com.kampplus.hava.model

import com.kampplus.hava.testing.sampleForecast
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FarmAdviceTest {
    private fun forecast(code: Int = 0, temperature: Double = 20.0, wind: Double? = null): Forecast {
        val sample = sampleForecast()
        return sample.copy(current = sample.current.copy(weatherCode = code, temperatureC = temperature, windSpeedKmh = wind))
    }

    @Test fun `yagmurlu gunde semsiye onerilir`() {
        val advice = FarmAdvice.from(forecast(61))
        assertTrue(advice.story.contains("yağmur"))
        assertTrue(advice.preparations.any { it.contains("Şemsiyeni") })
    }

    @Test fun `kar firtina soguk sicak ve ruzgar farkli hazirliklar uretir`() {
        assertTrue(FarmAdvice.from(forecast(71)).preparations.any { it.contains("ayakkabı") })
        assertTrue(FarmAdvice.from(forecast(95)).preparations.any { it.contains("ertele") })
        assertTrue(FarmAdvice.from(forecast(0, 4.0)).preparations.any { it.contains("kat al") })
        assertTrue(FarmAdvice.from(forecast(0, 32.0)).preparations.any { it.contains("Su şişeni") })
        assertTrue(FarmAdvice.from(forecast(wind = 40.0)).preparations.any { it.contains("sabitle") })
    }

    @Test fun `yagis olasiligi sadece sehrin bugunune aitse kullanilir`() {
        val sample = forecast()
        val date = sample.current.observedAt.toLocalDate()
        val rainyDay = DailyForecast(date, 10.0, 20.0, 61, 80)
        assertTrue(FarmAdvice.from(sample.copy(daily = listOf(rainyDay))).preparations.any { it.contains("şemsiye") })
        assertFalse(
            FarmAdvice.from(sample.copy(daily = listOf(rainyDay.copy(date = date.plusDays(1))))).preparations.any {
                it.contains("şemsiye")
            }
        )
    }

    @Test fun `eksik opsiyonel verilerde de anlamli bir gunluk olusur`() {
        val advice = FarmAdvice.from(forecast(12345))
        assertTrue(advice.story.isNotBlank())
        assertTrue(advice.preparations.isNotEmpty())
    }
}
