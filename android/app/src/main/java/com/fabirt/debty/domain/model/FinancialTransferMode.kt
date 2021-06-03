package com.fabirt.debty.domain.model

enum class FinancialTransferMode(val scheme: String) {
    ReceiveMoney("http://schema.googleapis.com/ReceiveMoney"),
    SendMoney("http://schema.googleapis.com/SendMoney"),
    AddMoney("http://schema.googleapis.com/AddMoney")
}