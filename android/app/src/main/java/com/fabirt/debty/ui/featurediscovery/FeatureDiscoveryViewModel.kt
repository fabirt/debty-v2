package com.fabirt.debty.ui.featurediscovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabirt.debty.domain.model.FeatureToDiscover
import com.fabirt.debty.domain.repository.featurediscovery.FeatureDiscoveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeatureDiscoveryViewModel @Inject constructor(
    private val repository: FeatureDiscoveryRepository
) : ViewModel() {

    suspend fun isFeatureDiscovered(
        featureToDiscover: FeatureToDiscover
    ) = repository.isFeatureDiscovered(featureToDiscover)

    fun storeFeatureAsDiscovered(featureToDiscover: FeatureToDiscover) {
        viewModelScope.launch {
            repository.storeFeatureAsDiscovered(featureToDiscover)
        }
    }
}