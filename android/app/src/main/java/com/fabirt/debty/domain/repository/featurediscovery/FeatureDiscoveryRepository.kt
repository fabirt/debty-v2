package com.fabirt.debty.domain.repository.featurediscovery

import com.fabirt.debty.domain.model.FeatureToDiscover

interface FeatureDiscoveryRepository {

    suspend fun isFeatureDiscovered(featureToDiscover: FeatureToDiscover): Boolean

    suspend fun storeFeatureAsDiscovered(featureToDiscover: FeatureToDiscover)
}