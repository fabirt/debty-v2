package com.fabirt.debty.ui.people.create

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabirt.debty.domain.model.Person
import com.fabirt.debty.domain.repository.person.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePersonViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {

    private var name: String? = null
    private val _picture: MutableLiveData<Bitmap?> = MutableLiveData()
    val picture: LiveData<Bitmap?> get() = _picture

    fun changeName(value: String?) {
        if (value != null && value.isNotBlank()) {
            name = value
        }
    }

    fun changePicture(value: Bitmap) {
        _picture.value = value
    }

    suspend fun saveChanges(): Boolean {
        if (name != null) {
            val person = Person(0, name!!, picture = _picture.value)
            personRepository.createPerson(person)
            return true
        }
        return false
    }
}