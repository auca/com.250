package com.toksaitov.countries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.toksaitov.countries.ui.theme.CountriesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val countriesViewModel = CountriesViewModel()
        setContent {
            CountriesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CountriesView(countriesViewModel)
                }
            }
        }
    }
}

@Composable
fun CountriesView(countriesViewModel: CountriesViewModel) {
    LaunchedEffect(Unit, block = {
        countriesViewModel.getCountriesList()
    })

    if (countriesViewModel.countriesList.isNotEmpty()) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            items(countriesViewModel.countriesList) { country ->
                Text(text = country.name.common)
            }
        }
    } else {
        Text(text = "Loading...")
    }
}
