package com.fabirt.debty.error

sealed class AppException : Exception() {

    object PermissionNotGrantedException : AppException()
    object ShouldRequestPermissionRationaleException : AppException()
}