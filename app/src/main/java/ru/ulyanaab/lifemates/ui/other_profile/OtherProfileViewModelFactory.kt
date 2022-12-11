package ru.ulyanaab.lifemates.ui.other_profile

import dagger.assisted.AssistedFactory

@AssistedFactory
interface OtherProfileViewModelFactory {
    fun create(userId: Long): OtherProfileViewModel
}
