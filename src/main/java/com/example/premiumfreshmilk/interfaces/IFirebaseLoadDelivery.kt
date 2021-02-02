package com.example.premiumfreshmilk.interfaces

import com.example.premiumfreshmilk.data.Transporters

interface IFirebaseLoadDelivery {
    fun onDelivLoadSuccess(transporterList: List<Transporters>)
    fun onDelivLoadFailed(message: String)
}
