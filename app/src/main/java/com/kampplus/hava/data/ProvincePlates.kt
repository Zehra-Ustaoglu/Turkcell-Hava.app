package com.kampplus.hava.data

import com.kampplus.hava.model.City
import java.text.Normalizer
import java.util.Locale

/** Index + 1 is the official plate number; leading zeroes are accepted in search. */
object ProvincePlates {
    val names = listOf(
        "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
        "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa",
        "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan",
        "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta",
        "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir",
        "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla",
        "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop",
        "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat",
        "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın",
        "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"
    )

    fun nameFor(query: String): String? {
        val text = query.trim()
        if (text.length !in 1..2 || text.any { it !in '0'..'9' }) return null
        return text.toIntOrNull()?.let { names.getOrNull(it - 1) }
    }

    fun plateFor(city: City): String? {
        if (city.countryCode != "TR") return null
        if (city.id < 0) return (-city.id).toString().padStart(2, '0')
        if (city.featureCode !in listOf("PPLA", "PPLC")) return null
        val aliases = mapOf("izmit" to "Kocaeli", "adapazari" to "Sakarya", "antakya" to "Hatay", "afyon" to "Afyonkarahisar")
        val name = normalized(city.name)
        val canonical = aliases[name] ?: city.name
        val index = names.indexOfFirst { normalized(it) == normalized(canonical) }
        return if (index >= 0) (index + 1).toString().padStart(2, '0') else null
    }

    private fun normalized(value: String): String = Normalizer.normalize(value.lowercase(Locale.forLanguageTag("tr")), Normalizer.Form.NFD)
        .replace(Regex("\\p{M}"), "").replace('ı', 'i')
}
