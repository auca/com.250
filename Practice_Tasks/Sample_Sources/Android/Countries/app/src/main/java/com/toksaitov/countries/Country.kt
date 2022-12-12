package com.toksaitov.countries

data class CountryName(var common: String, var official: String)

data class Country(var name: CountryName, var flag: String)
