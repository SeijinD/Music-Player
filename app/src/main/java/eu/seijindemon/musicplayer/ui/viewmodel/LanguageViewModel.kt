package eu.seijindemon.musicplayer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.seijindemon.musicplayer.data.repository.DataStorePreferenceRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val dataStorePreferenceRepository: DataStorePreferenceRepository
): ViewModel() {

    private val _language = MutableLiveData(0)
    var language: LiveData<Int> = _language

    init {
        viewModelScope.launch {
            dataStorePreferenceRepository.getLanguage.collect {
                _language.value = it
            }
        }
    }

    suspend fun saveLanguage(language: Int) {
        dataStorePreferenceRepository.setLanguage(language = language)
    }

}