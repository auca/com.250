package com.toksaitov.doodler.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DoodleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DoodleRepository

    val allDoodles: LiveData<List<Doodle>>

    init {
        val doodleDao = DoodleRoomDatabase.getDatabase(application).doodleDao()
        repository = DoodleRepository(doodleDao)
        allDoodles = repository.allDoodles
    }

    fun insert(doodle: Doodle) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(doodle)
    }

    fun delete(doodle: Doodle) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(doodle)
    }
}
