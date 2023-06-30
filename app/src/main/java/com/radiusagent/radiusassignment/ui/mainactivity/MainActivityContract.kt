package com.radiusagent.radiusassignment.ui.mainactivity

import com.radiusagent.radiusassignment.data.model.ApiResponse
import com.radiusagent.radiusassignment.data.model.Facility

interface MainActivityContract {
    interface View {
        fun showData(data: ApiResponse?)
    }

    interface Model {
        fun fetchApi( myCallback: (result: ApiResponse?) -> Unit)
        fun fetchLocally(): ApiResponse?
        fun saveDataLocally(apiResponse: ApiResponse)
    }

    interface Presenter {
        fun getData()
        fun onDestroy()
    }
}