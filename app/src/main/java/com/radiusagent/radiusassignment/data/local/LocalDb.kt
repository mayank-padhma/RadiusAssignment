package com.radiusagent.radiusassignment.data.local

import com.radiusagent.radiusassignment.data.model.ApiResponse
import com.radiusagent.radiusassignment.data.model.Exclusion
import com.radiusagent.radiusassignment.data.model.ExclusionDb
import com.radiusagent.radiusassignment.data.model.Facility
import com.radiusagent.radiusassignment.data.model.FacilityDb
import com.radiusagent.radiusassignment.data.model.ListExclusionDb
import com.radiusagent.radiusassignment.data.model.LocalData
import com.radiusagent.radiusassignment.data.model.Option
import com.radiusagent.radiusassignment.data.model.OptionDb
import io.realm.Realm
import io.realm.RealmList

class LocalDb {
    // save data to realmdb
    fun saveData(apiResponse: ApiResponse){
        val realmData = LocalData()
        realmData.facilities = RealmList<FacilityDb>()
        realmData.exclusions = RealmList<ListExclusionDb>()

//        convert apiResponse data to realmdb classes
        apiResponse.facilities.forEach {
            val options = RealmList<OptionDb>()
            it.options.forEach{opt ->
                val option = OptionDb()
                option.id = opt.id
                option.icon = opt.icon
                option.name = opt.name
                options.add(option)
            }
            val facility = FacilityDb()
            facility.facilityId = it.facilityId
            facility.name = it.name
            facility.options = options
            realmData.facilities!!.add(facility)
        }

        apiResponse.exclusions.forEach {
            val listOfExclusion = RealmList<ExclusionDb>()
            it.forEach {ex ->
                val exclusion = ExclusionDb()
                exclusion.facilityId = ex.facilityId
                exclusion.optionsId = ex.optionsId
                listOfExclusion.add(exclusion)
            }
            val listExclusionDb = ListExclusionDb()
            listExclusionDb.listOfExclusion = listOfExclusion
            realmData.exclusions!!.add(listExclusionDb)
        }

        // add last sync timestamp
        realmData.lastSync = System.currentTimeMillis()

        // add data to realmdb
        val realm = Realm.getDefaultInstance()
        // Write data to the database
        realm.executeTransaction {
            realm.where(LocalData::class.java).findAll().deleteAllFromRealm()
            it.insertOrUpdate(realmData)
        }
        // Close the Realm instance when no longer needed
        realm.close()
    }

    // fetch data from realmdb
    fun getData() : ApiResponse?{
        val realm = Realm.getDefaultInstance()
        // Query data from the database
        val result = realm.where(LocalData::class.java).findFirst()
        val facilities = result?.facilities
        val exclusions = result?.exclusions
        val lastSync = result?.lastSync

        return if (facilities.isNullOrEmpty() || exclusions.isNullOrEmpty() || lastSync==null){
            realm.close()
            null
        }else {
            val facilitiesRes = arrayListOf<Facility>()
            val exclusionsRes = arrayListOf<ArrayList<Exclusion>>()

            facilities.forEach {
                val options = arrayListOf<Option>()
                it.options?.forEach{opt ->
                    val option = Option(opt.name, opt.icon, opt.id)
                    options.add(option)
                }
                facilitiesRes.add(Facility(it.facilityId
                    , it.name, options))
            }

            exclusions.forEach {
                val listOfExclusion = arrayListOf<Exclusion>()
                it.listOfExclusion?.forEach {ex ->
                    listOfExclusion.add(Exclusion(ex.facilityId, ex.optionsId))
                }
                exclusionsRes.add(listOfExclusion)
            }
            realm.close()
            ApiResponse(facilitiesRes, exclusionsRes, lastSync)
        }
    }

}