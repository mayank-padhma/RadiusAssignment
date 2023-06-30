package com.radiusagent.radiusassignment.data

import com.radiusagent.radiusassignment.data.local.LocalDb
import com.radiusagent.radiusassignment.data.model.ApiResponse
import com.radiusagent.radiusassignment.data.remote.ApiManager
import com.radiusagent.radiusassignment.ui.mainactivity.MainActivityContract
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class Repository(private var apiManager: ApiManager, private var compositeDisposable: CompositeDisposable) : MainActivityContract.Model {
    private var localDb = LocalDb()

    // fetches data from api
    override fun fetchApi(myCallback: (result: ApiResponse?) -> Unit) {
        apiManager.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<ApiResponse> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    myCallback(null)
                }

                override fun onSuccess(t: ApiResponse) {
                    myCallback(t)
                    saveDataLocally(t)
                }
            })
    }

    // fetches data from local db(realm)
    override fun fetchLocally(): ApiResponse? {
        return localDb.getData()
    }

    // saves data to local db(realm)
    override fun saveDataLocally(apiResponse: ApiResponse) {
        localDb.saveData(apiResponse)
    }
}