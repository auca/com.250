package com.toksaitov.countries

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://restcountries.com/v3.1/"

interface CountriesService {
    @GET("all")
    suspend fun listCountries(@Query("fields") fields: String): List<Country>

    companion object {
        private var countryService: CountriesService? = null

        fun getInstance(): CountriesService {
            if (countryService == null) {
                countryService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CountriesService::class.java);
            }
            return countryService!!;
        }
    }
}
