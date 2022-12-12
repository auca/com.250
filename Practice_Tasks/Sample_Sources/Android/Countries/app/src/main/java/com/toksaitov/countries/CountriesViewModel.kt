package com.toksaitov.countries

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.lang.Exception

class CountriesViewModel: ViewModel() {
    private val _countriesList = mutableStateListOf<Country>()
    val countriesList: List<Country>
        get() = _countriesList

    var errorMessage: String by mutableStateOf("")

    suspend fun getCountriesList() {
        val apiService = CountriesService.getInstance()
        try {
            _countriesList.clear()
            _countriesList.addAll(apiService.listCountries("name,flag"))
        } catch (e: Exception) {
            errorMessage = e.message.toString()
        }
    }
}