package com.example.premiumfreshmilk.data

import com.google.firebase.database.Exclude
import java.io.Serializable

class MilkParameter : Serializable {
    var id: String? = ""
    var dateRecorded: String? = ""
    var transporterQName: String? = ""
    var transporterQNumber: String? = ""
    var qualityWeight: String? = ""
    var qualityButterfat: String? = ""
    var qualityDensity: String? = ""
    var qualitySmell: String? = ""
    var qualityTaste: String? = ""

    @get:Exclude
    @set:Exclude
    var key: String? = ""

    constructor() { //empty constructor needed
    }

    constructor(
        key: String?,
        dateRecorded: String?,
        transporterQName: String?,
        transporterQNumber: String?,
        qualityWeight: String?,
        qualityButterfat: String?,
        qualityDensity: String?,
        qualitySmell: String?,
        qualityTaste: String?
    ) {
        this.key = key
        this.dateRecorded = dateRecorded
        this.transporterQName = transporterQName
        this.transporterQNumber = transporterQNumber
        this.qualityWeight = qualityWeight
        this.qualityButterfat = qualityButterfat
        this.qualityDensity = qualityDensity
        this.qualitySmell = qualitySmell
        this.qualityTaste = qualityTaste
    }

}