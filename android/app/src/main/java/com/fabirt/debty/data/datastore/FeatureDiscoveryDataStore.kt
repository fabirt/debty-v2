package com.fabirt.debty.data.datastore

import com.fabirt.debty.domain.model.FeatureToDiscover

interface FeatureDiscoveryDataStore {

    suspend fun isFeatureDiscovered(featureToDiscover: FeatureToDiscover): Boolean

    suspend fun setFeatureAsDiscovered(featureToDiscover: FeatureToDiscover)
}