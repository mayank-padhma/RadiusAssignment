package com.radiusagent.radiusassignment.data.model

import io.realm.RealmList
import io.realm.RealmObject

// Classes to save data in realmdb
open class LocalData : RealmObject() {
    var facilities : RealmList<FacilityDb>? = null
    var exclusions : RealmList<ListExclusionDb>? = null
    var lastSync : Long = 0L
}

open class ExclusionDb : RealmObject() {
    var facilityId : String = ""
    var optionsId : String = ""
}

open class ListExclusionDb : RealmObject() {
    var listOfExclusion : RealmList<ExclusionDb>? = null
}

open class FacilityDb : RealmObject() {
    var facilityId: String = ""
    var name: String = ""
    var options: RealmList<OptionDb>? = null
}

open class OptionDb : RealmObject() {
    var name: String = ""
    var icon: String = ""
    var id: String = ""
}