package com.example.premiumfreshmilk.data

import com.google.firebase.database.Exclude
import java.io.Serializable

class ReceivedMilk : Serializable {
    var id: String? = ""
    var datedelivered: String? = ""
    var transporterName: String? = ""
    var transporterNumber: String? = ""
    var deliveredWeight: String? = ""

    @get:Exclude
    @set:Exclude
    var key: String? = ""

    constructor() { //empty constructor needed
    }

    constructor(
        key: String?,
        datedelivered: String?,
        transporterName: String?,
        transporterNumber: String?,
        deliveredWeight: String?
    ) {
        this.key = key
        this.datedelivered = datedelivered
        this.transporterName = transporterName
        this.transporterNumber = transporterNumber
        this.deliveredWeight = deliveredWeight
    }

//    override fun toString(): String {
//        return nameFarmer!!
//    }

}