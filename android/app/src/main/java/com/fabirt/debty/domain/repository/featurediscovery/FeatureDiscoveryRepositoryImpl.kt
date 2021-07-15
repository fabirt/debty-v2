package com.fabirt.debty.domain.repository.featurediscovery

import com.fabirt.debty.data.datastore.FeatureDiscoveryDataStore
import com.fabirt.debty.domain.model.FeatureToDiscover
import com.fabirt.debty.domain.repository.BaseRepository
import com.fabirt.debty.util.getOrElse
import com.fabirt.debty.util.right

class FeatureDiscoveryRepositoryImpl(
    private val dataStore: FeatureDiscoveryDataStore
) : BaseRepository(), FeatureDiscoveryRepository {
    override suspend fun isFeatureDiscovered(featureToDiscover: FeatureToDiscover): Boolean {
        return runCatching {
            right(dataStore.isFeatureDiscovered(featureToDiscover))
        }.getOrElse(false)
    }

    override suspend fun storeFeatureAsDiscovered(featureToDiscover: FeatureToDiscover) {
        runCatching {
            dataStore.storeFeatureAsDiscovered(featureToDiscover)
            right(Unit)
        }
    }
}