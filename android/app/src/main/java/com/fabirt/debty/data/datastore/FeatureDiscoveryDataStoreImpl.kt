package com.fabirt.debty.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.fabirt.debty.domain.model.FeatureToDiscover
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FeatureDiscoveryDataStoreImpl(
    private val context: Context
) : FeatureDiscoveryDataStore {

    private val keyCreateMovementDiscovered = booleanPreferencesKey("create_movement_discovered")
    private val keyCreatePersonDiscovered = booleanPreferencesKey("create_person_discovered")
    private val keyPersonDetailDiscovered = booleanPreferencesKey("person_detail_discovered")
    private val keyDrawerMenuDiscovered = booleanPreferencesKey("drawer_menu_discovered")

    override suspend fun isFeatureDiscovered(featureToDiscover: FeatureToDiscover): Boolean {
        return context.fdDataStore.data.map { preferences ->
            val featureKey = when(featureToDiscover) {
                FeatureToDiscover.CreateMovement -> keyCreateMovementDiscovered
                FeatureToDiscover.CreatePerson -> keyCreatePersonDiscovered
                FeatureToDiscover.PersonDetail -> keyPersonDetailDiscovered
                FeatureToDiscover.DrawerMenu -> keyDrawerMenuDiscovered
            }
            preferences[featureKey] ?: false
        }.first()
    }

    override suspend fun storeFeatureAsDiscovered(featureToDiscover: FeatureToDiscover) {
        context.fdDataStore.edit { preferences ->
            val featureKey = when(featureToDiscover) {
                FeatureToDiscover.CreateMovement -> keyCreateMovementDiscovered
                FeatureToDiscover.CreatePerson -> keyCreatePersonDiscovered
                FeatureToDiscover.PersonDetail -> keyPersonDetailDiscovered
                FeatureToDiscover.DrawerMenu -> keyDrawerMenuDiscovered
            }

            preferences[featureKey] = true
        }
    }
}

private val Context.fdDataStore: DataStore<Preferences> by preferencesDataStore(name = "feature_discovery")
