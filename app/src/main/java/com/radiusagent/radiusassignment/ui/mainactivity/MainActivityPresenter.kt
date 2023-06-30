package com.radiusagent.radiusassignment.ui.mainactivity

import com.radiusagent.radiusassignment.data.Repository
import com.radiusagent.radiusassignment.data.remote.ApiManager
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivityPresenter(private var mainView: MainActivityContract.View) : MainActivityContract.Presenter {
    private var apiManager = ApiManager()
    private var compositeDisposable = CompositeDisposable()
    private var mainActivityModel : MainActivityContract.Model = Repository(apiManager, compositeDisposable)

    // get data from either local db or remote db
    override fun getData() {
        val data = mainActivityModel.fetchLocally()
        if (data == null || data.lastSync < System.currentTimeMillis() - 86400000){
            mainActivityModel.fetchApi {
                mainView.showData(it)
            }
        }else {
            mainView.showData(data)
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}