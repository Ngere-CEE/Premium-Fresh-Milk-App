package com.example.premiumfreshmilk.data

import com.google.firebase.database.Exclude
import java.io.Serializable

class Scientist : Serializable {
    var id: String? = ""
    var docln: String? = ""
    var fullName: String? = ""
    var farmerNumber: String? = ""
    var weight: String? = ""

    @get:Exclude
    @set:Exclude
    var key: String? = ""

    constructor() { //empty constructor needed
    }

    constructor(
        key: String?,
        docln: String?,
        fullName: String?,
        farmerNumber: String?,
        weight: String?
    ) {
        this.key = key
        this.docln = docln
        this.fullName = fullName
        this.farmerNumber = farmerNumber
        this.weight = weight
    }

//    override fun toString(): String {
//        return nameFarmer!!
//    }

}