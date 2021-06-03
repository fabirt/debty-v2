package com.fabirt.debty.ui.assistant

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabirt.debty.domain.model.FinancialTransferMode
import com.fabirt.debty.domain.model.Movement
import com.fabirt.debty.domain.model.MovementType
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.domain.repository.movement.MovementRepository
import com.fabirt.debty.domain.repository.person.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val movementRepository: MovementRepository
) : ViewModel() {

    companion object {
        private const val TAG = "AssistantViewModel"
    }

    sealed class Event {
        data class AssistantEvent(
            val transferAmount: Double,
            val transferDestinationName: String,
            val transferMode: FinancialTransferMode
        ) : Event()
    }

    private val eventChannel = Channel<Event>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun receiveMoneyTransferIntent(bundle: Bundle?) {
        val transferAmount = bundle?.getString("transferAmount")?.toDoubleOrNull()
        val transferOriginName = bundle?.getString("moneyTransferOriginName")
        val transferDestinationName = bundle?.getString("moneyTransferDestinationName")
        val transferMode = bundle?.getString("transferMode")

        val message =
            "Amount: $transferAmount | Origin: $transferOriginName | Destination: $transferDestinationName | Mode: $transferMode"
        Log.i(TAG, message)

        if (transferAmount == null || transferMode == null) return

        val movementType: MovementType
        val financialTransferMode: FinancialTransferMode

        when (transferMode) {
            FinancialTransferMode.ReceiveMoney.scheme -> {
                movementType = MovementType.LentMe
                financialTransferMode = FinancialTransferMode.ReceiveMoney
            }
            FinancialTransferMode.SendMoney.scheme -> {
                movementType = MovementType.IPaid
                financialTransferMode = FinancialTransferMode.SendMoney
            }
            FinancialTransferMode.AddMoney.scheme -> {
                movementType = MovementType.ILent
                financialTransferMode = FinancialTransferMode.AddMoney
            }
            else -> {
                movementType = MovementType.ILent
                financialTransferMode = FinancialTransferMode.AddMoney
            }
        }

        if (transferDestinationName == null) return

        viewModelScope.launch {
            eventChannel.send(
                Event.AssistantEvent(
                    transferAmount,
                    transferDestinationName,
                    financialTransferMode
                )
            )
        }

        createCompleteMovement(transferAmount, transferDestinationName, movementType)
    }

    private fun createCompleteMovement(
        transferAmount: Double,
        transferDestinationName: String,
        movementType: MovementType
    ) {
        viewModelScope.launch {
            val personFound = personRepository.searchPerson(transferDestinationName).firstOrNull()

            val personId = if (personFound != null) {
                personFound.id
            } else {
                val personToCreate = Person(transferDestinationName)
                personRepository.createPerson(personToCreate).getOrNull()?.toInt() ?: return@launch
            }

            val movement = Movement(
                0,
                personId,
                transferAmount * movementType.multiplier,
                System.currentTimeMillis(),
                "Creado por Google Assistant",
                movementType
            )
            movementRepository.createMovement(movement)
        }
    }
}