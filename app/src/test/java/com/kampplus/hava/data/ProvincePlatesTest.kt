package com.kampplus.hava.data

import com.kampplus.hava.testing.sampleCity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ProvincePlatesTest {
    @Test fun `tum plaka kodlari tek bir ile eslenir`() {
        assertEquals(81, ProvincePlates.names.distinct().size)
        (1..81).forEach { assertNotNull(ProvincePlates.nameFor(it.toString())) }
        assertEquals("İstanbul", ProvincePlates.nameFor("34"))
        assertEquals("Ankara", ProvincePlates.nameFor(" 06 "))
        assertEquals("Ankara", ProvincePlates.nameFor("6"))
        assertEquals("Düzce", ProvincePlates.nameFor("81"))
        listOf("0", "00", "82", "99", "006", "-1", "34a", "Ankara").forEach {
            assertNull(ProvincePlates.nameFor(it))
        }
    }

    @Test fun `ilce ve yabanci sehir plaka rozeti almaz`() {
        assertNull(ProvincePlates.plateFor(sampleCity().copy(featureCode = "PPLA2")))
        assertNull(ProvincePlates.plateFor(sampleCity().copy(countryCode = "US", featureCode = "PPLA")))
        assertEquals("06", ProvincePlates.plateFor(sampleCity().copy(featureCode = "PPLC")))
        assertEquals("41", ProvincePlates.plateFor(sampleCity(name = "İzmit").copy(featureCode = "PPLA")))
        assertEquals("54", ProvincePlates.plateFor(sampleCity(name = "Adapazarı").copy(featureCode = "PPLA")))
    }
}
