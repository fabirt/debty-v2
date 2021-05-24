package com.fabirt.debty

import com.fabirt.debty.data.db.entities.DBMovement
import com.fabirt.debty.data.db.entities.DBPerson
import com.fabirt.debty.data.db.entities.DBPersonWithTotal
import com.fabirt.debty.data.model.MovementTypeDto

object TestUtil {

    fun createDBMovement(id: Int) = DBMovement(
        1, 20_000.0, 0, "Test Description", MovementTypeDto(1), id
    )

    fun createDBMovementListForSinglePerson(id: Int) = listOf(
        DBMovement(id, 10_000.0, 0, "Test Description 1", MovementTypeDto(1), 1),
        DBMovement(id, 20_000.0, 1, "Test Description 2", MovementTypeDto(1), 2),
        DBMovement(id, 30_000.0, 2, "Test Description 3", MovementTypeDto(1), 3),
    )

    fun createDBMovementListForSinglePersonUnsorted(id: Int) = listOf(
        DBMovement(id, 30_000.0, 9, "Test Description 1", MovementTypeDto(1), 1),
        DBMovement(id, 10_000.0, 3, "Test Description 2", MovementTypeDto(1), 2),
        DBMovement(id, 20_000.0, 5, "Test Description 3", MovementTypeDto(1), 3),
    )

    fun createDBPerson(id: Int) = DBPerson(
        "Test", 0, null, id
    )

    fun createMultipleDBPerson() = listOf(
        DBPerson("Test 1", 0, null, 1),
        DBPerson("Test 2", 1, null, 2),
        DBPerson("Test 3", 2, null, 3),
    )

    fun createMultipleDBMovement() = listOf(
        DBMovement(1, 30_000.0, 9, "Test Description 1", MovementTypeDto(1), 1),
        DBMovement(1, 10_000.0, 3, "Test Description 2", MovementTypeDto(1), 2),
        DBMovement(2, 20_000.0, 5, "Test Description 3", MovementTypeDto(1), 3),
    )

    fun createMultipleDBPersonWithTotal() = listOf(
        DBPersonWithTotal(
            DBPerson("Test 1", 0, null, 1),
            40_000.0
        ),
        DBPersonWithTotal(
            DBPerson("Test 2", 1, null, 2),
            20_000.0
        ),
    )
}