package com.documentationplease

import android.content.ContentValues

sealed class datas {
    data class ci_mi (val ci_mi: String? = null): datas()
    data class country (val country: String, val cities: List<ci_mi>?, val passport: Int, val seal_1: Int? = null, val seal_2: Int? = null): datas()

}

data class mision_contry (val country: String, val mision: String, val rest_country: String? = null, val second_conutry: String? = null, val second_mision: String? = null)
data class global_data (val take_misions: Int,  val mision_extra: mision_contry? = null, val day_mision: String = "There are no missions today")

val acu_misions = listOf(
    datas.ci_mi("Passport required to enter"),
    datas.ci_mi("Documents must be in order"),
    datas.ci_mi("Arstotzkans must have an I.D. card"),
    datas.ci_mi("Work permit required for workers"),
    datas.ci_mi("No weapons or contraband"),
    datas.ci_mi("No diplomats without authorization"),
    datas.ci_mi("Grant of asylum required for asylum"),
    datas.ci_mi("Polio vaccination required"),
   datas.ci_mi("Access permit required for foreigners")
)

val country_info = listOf(
    datas.country("Arstotzka", listOf(datas.ci_mi("Altan"), datas.ci_mi("Vescillo"), datas.ci_mi("Burton"), datas.ci_mi("Octovalis"), datas.ci_mi("Gennistora"), datas.ci_mi("Lendiforma"), datas.ci_mi("Wozenfield"), datas.ci_mi("Fardesto")), R.drawable.arstotzka_passport),
    datas.country("Antegria", listOf(datas.ci_mi("San Marmero"), datas.ci_mi("Gloriana"), datas.ci_mi("Fora Grouse")), R.drawable.antegrian_passport, R.drawable.antegria_seal_1, R.drawable.antegria_seal_2),
    datas.country("Impor", listOf(datas.ci_mi("Enkyo"), datas.ci_mi("Haihan"), datas.ci_mi("Tsunkeido")), R.drawable.impor_passport, R.drawable.impor_seal_1, R.drawable.impor_seal_2),
    datas.country("Kolechia", listOf(datas.ci_mi("Yurko"), datas.ci_mi("Vedor")), R.drawable.kolechia_passport, R.drawable.kolechia_seal_1, R.drawable.kolechia_seal_2),
    datas.country("Republia", null, R.drawable.republia_passport, R.drawable.republia_seal_1, R.drawable.republia_seal_2),
    datas.country("United Federation", listOf(datas.ci_mi("Yurko"), datas.ci_mi("Vedor")), R.drawable.united_federation_passport, R.drawable.ufed_seal_1, R.drawable.ufed_seal_2),
    datas.country("Obristán", null, R.drawable.cobrastan_passport)
)

val global_info = mapOf(
    1 to global_data(1, mision_contry("Arstotzka", "Arstotzkan citizens only", "Deny all foreigners")),
    2 to global_data(2),
    3 to global_data(2),
    4 to global_data(3),
    5 to global_data(3, null, "Arrest passport forgers and criminals"),
    6 to global_data(4),
    7 to global_data(5, mision_contry("Kolechia", "Search all Kolechians"), "Search for weapons and contraband"),
    8 to global_data(6),
    9 to global_data(6),
    10 to global_data(6),
    11 to global_data(6),
    12 to global_data(6),
    13 to global_data(6),
    14 to global_data(6, null, "Check daily bulletin for wanted criminals"),
    15 to global_data(6),
    16 to global_data(6),
    17 to global_data(6),
    18 to global_data(6),
    19 to global_data(6, mision_contry("Impor", "Deny all Imporian citizens")),
    20 to global_data(6),
    21 to global_data(7),
    22 to global_data(7),
    23 to global_data(7),
    24 to global_data(7, mision_contry("Arstotzka", "(Altan District) Seize passports from Arstotzkan citizens of Altan district")),
    25 to global_data(7, mision_contry("Arstotzka", "(Altan District) Seize passports from Arstotzkan citizens of Altan district", null, "United Federation", "Deny all United Federation citizens")),
    26 to global_data(8, mision_contry("Arstotzka", "(Altan District) Seize passports from Arstotzkan citizens of Altan district")),
    27 to global_data(9, mision_contry("Arstotzka", "(Altan District) Seize passports from Arstotzkan citizens of Altan district")),
    28 to global_data(9, mision_contry("Arstotzka", "Seize all Arstotzkan passports")),
    29 to global_data(9, mision_contry("Arstotzka", "Seize all Arstotzkan passports")),
    30 to global_data(9, mision_contry("Arstotzka", "Seize all Arstotzkan passports")),
    31 to global_data(9, mision_contry("Arstotzka", "Seize all Arstotzkan passports")),
)