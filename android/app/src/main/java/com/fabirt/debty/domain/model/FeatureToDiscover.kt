package com.fabirt.debty.domain.model

sealed class FeatureToDiscover {
    object CreateMovement : FeatureToDiscover()
    object CreatePerson : FeatureToDiscover()
    object PersonDetail : FeatureToDiscover()
    object DrawerMenu : FeatureToDiscover()
}
