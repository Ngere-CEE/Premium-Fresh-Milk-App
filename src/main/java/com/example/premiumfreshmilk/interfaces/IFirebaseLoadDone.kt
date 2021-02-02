package com.example.premiumfreshmilk.interfaces

import com.example.premiumfreshmilk.data.farmer

interface IFirebaseLoadDone {
    fun onFirebaseLoadSuccess(farmerList: List<farmer>)
    fun onFirebaseLoadFailed(message: String)
}
