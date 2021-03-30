package com.fabirt.debty.domain.repository

import com.fabirt.debty.error.Failure
import com.fabirt.debty.util.Either
import com.fabirt.debty.util.left

abstract class BaseRepository {

    protected suspend fun <T> runCatching(
        block: suspend () -> Either<Failure, T>
    ): Either<Failure, T> {
        return try {
            block()
        } catch (e: Exception) {
            left(Failure.UnexpectedFailure)
        }
    }
}