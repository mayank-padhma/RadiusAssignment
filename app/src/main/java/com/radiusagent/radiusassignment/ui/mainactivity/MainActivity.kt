package com.radiusagent.radiusassignment.ui.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.radiusagent.radiusassignment.R
import com.radiusagent.radiusassignment.adapters.FacilitiesAdapter
import com.radiusagent.radiusassignment.adapters.OnClickListener
import com.radiusagent.radiusassignment.data.model.ApiResponse
import com.radiusagent.radiusassignment.data.model.Exclusion
import com.radiusagent.radiusassignment.data.model.Option
import com.radiusagent.radiusassignment.data.remote.ApiManager
import com.radiusagent.radiusassignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainActivityContract.View, OnClickListener {

    private lateinit var adapter3: FacilitiesAdapter
    private lateinit var adapter2: FacilitiesAdapter
    private lateinit var adapter1: FacilitiesAdapter

    private lateinit var binding: ActivityMainBinding
    private lateinit var contractPresenter: MainActivityContract.Presenter
    private lateinit var apiManager: ApiManager

    private lateinit var fac1RecyclerView: RecyclerView
    private lateinit var fac2RecyclerView: RecyclerView
    private lateinit var fac3RecyclerView: RecyclerView

    private var listFacility1 = arrayListOf<Option>()
    private var listFacility2 = arrayListOf<Option>()
    private var listFacility3 = arrayListOf<Option>()

    // for exclusions
    private var checkedList = arrayListOf<Exclusion?>(null, null, null)
    private var recyclerPosList = arrayListOf(-1, -1, -1)

    private var apiResponse: ApiResponse = ApiResponse(arrayListOf(), arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiManager = ApiManager()
        contractPresenter = MainActivityPresenter(this)
        contractPresenter.getData()

        // initializes variables
        setUpViewAndAdapters()

    }

    private fun setUpViewAndAdapters(){
        if (!this::adapter1.isInitialized){
            fac1RecyclerView = binding.propTypeRecycler
            fac2RecyclerView = binding.numRoomsRecycler
            fac3RecyclerView = binding.otherFacRecycler
            adapter1 = FacilitiesAdapter(
                this,
                listFacility1,
                1,
                this
            )

            adapter2 = FacilitiesAdapter(
                this,
                listFacility2,
                2,
                this
            )
            adapter3 = FacilitiesAdapter(
                this,
                listFacility3,
                3,
                this
            )
            val layoutManager1 = GridLayoutManager(this, 2)
            val layoutManager2 = GridLayoutManager(this, 2)
            val layoutManager3 = GridLayoutManager(this, 2)

            fac1RecyclerView.adapter = adapter1
            fac2RecyclerView.adapter = adapter2
            fac3RecyclerView.adapter = adapter3

            fac1RecyclerView.layoutManager = layoutManager1
            fac2RecyclerView.layoutManager = layoutManager2
            fac3RecyclerView.layoutManager = layoutManager3
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contractPresenter.onDestroy()
    }

    // inflate data to the ui
    override fun showData(data: ApiResponse?) {
        if (data != null) {
            val listOfFacility = data.facilities
            // data binding
            binding.facility1 = listOfFacility[0].name
            binding.facility2 = listOfFacility[1].name
            binding.facility3 = listOfFacility[2].name
            binding.isVisible = true

            this.apiResponse = data

            listFacility1.clear()
            listFacility1.addAll(data.facilities[0].options)
            listFacility2.clear()
            listFacility2.addAll(data.facilities[1].options)
            listFacility3.clear()
            listFacility3.addAll(data.facilities[2].options)

            setUpViewAndAdapters()

            adapter1.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
            adapter3.notifyDataSetChanged()
        } else {
            binding.isVisible = false
            showAlertDialog()
        }
    }

    // when api call fails
    private fun showAlertDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Oops!!")
            .setMessage("Some error occurred while fetching data. Try changing your network.")
            .setPositiveButton("Retry") { dialog, _ ->
                contractPresenter.getData()
                dialog.dismiss()
            }.setCancelable(false)
            .create()

        dialog.show()
    }

    // onclick for cards in recycler view
    override fun onClick(optionId: Int, facilityId: Int, recyclerId: Int) {
        val recyclerView = when (facilityId) {
            1 -> fac1RecyclerView
            2 -> fac2RecyclerView
            3 -> fac3RecyclerView
            else -> return
        }

        // get view instance of selected item
        val view = recyclerView[recyclerId].findViewById<MaterialCardView>(R.id.cardView)
        // if the view is checked then simply uncheck
        if (view.isChecked) {
            view.isChecked = false
            checkedList[facilityId - 1] = null
            recyclerPosList[facilityId - 1] = -1
            return
        }

        // take a temporary list and add option assuming the view is selected
        val newCheckedList = arrayListOf<Exclusion?>()
        newCheckedList.addAll(checkedList)
        newCheckedList[facilityId - 1] = Exclusion(facilityId.toString(), optionId.toString())

        // loop through each exclusion
        apiResponse.exclusions.forEach {
            // check if exclusion is subset of selected views, if yes then show the toast
            if (
                it.all { item2 ->
                    newCheckedList.any { item1 ->
                        item1?.facilityId == item2.facilityId && item1.optionsId == item2.optionsId
                    }
                }
            ) {
                Toast.makeText(this, "This card cannot be selected.", Toast.LENGTH_SHORT).show()
                return
            }
        }
        // if it is safe to select then unselect prev item in the same facility and select current item
        if (checkedList[facilityId - 1] != null) {
            val viewPrev =
                recyclerView[recyclerPosList[facilityId - 1]].findViewById<MaterialCardView>(R.id.cardView)
            viewPrev.isChecked = false
        }
        checkedList[facilityId - 1] = Exclusion(facilityId.toString(), optionId.toString())
        recyclerPosList[facilityId - 1] = recyclerId
        view.isChecked = true
    }
}
