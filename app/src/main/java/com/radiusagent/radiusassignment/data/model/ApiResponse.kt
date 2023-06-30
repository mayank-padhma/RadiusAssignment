package com.radiusagent.radiusassignment.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmModel
import io.realm.annotations.RealmClass

// Classes for api response
data class ApiResponse (
    @SerializedName("facilities")
    var facilities : List<Facility>,
    @SerializedName("exclusions")
    var exclusions : List<List<Exclusion>>,
    var lastSync : Long = 0L
)

data class Exclusion(
    @SerializedName("facility_id")
    var facilityId : String,
    @SerializedName("options_id")
    var optionsId : String
)

data class Facility(
    @SerializedName("facility_id")
    var facilityId : String,
    @SerializedName("name")
    var name : String,
    @SerializedName("options")
    var options : List<Option>
)

data class Option (
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: String,
)