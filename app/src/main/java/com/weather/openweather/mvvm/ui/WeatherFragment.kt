package com.weather.openweather.mvvm.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.weather.openweather.R
import com.weather.openweather.databinding.FragmentWeatherBinding
import com.weather.openweather.helperclasses.Helper
import com.weather.openweather.helperclasses.SPHelper
import com.weather.openweather.helperclasses.SPHelper.getStringData
import com.weather.openweather.mvvm.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.apache.commons.lang3.StringUtils
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class WeatherFragment: Fragment() {

    private val citiesList = arrayOf(
        "New York",
        "Chicago",
        "Boston",
        "San Diego",
        "Los Angeles",
        "San Francisco",
        "Philadelphia",
        "Austin",
        "Seattle",
        "Nashville",
        "Phoenix",
        "San Jose",
        "San Antonio",
        "Oklahoma City",
        "Las Vegas",
        "Baltimore",
        "Houston",
        "New Orleans",
        "Washington",
        "Washington, D.C.",
        "Columbus",
        "Dallas",
        "Indianapolis",
        "Atlanta",
        "Detroit",
        "Denver",
        "Honolulu",
        "Jacksonville",
        "Fort Worth",
        "El Paso",
        "Kansas City",
        "Milwaukee",
        "Memphis",
        "Sacramento",
        "Charlotte",
        "Portland",
        "Fresno",
        "Raleigh",
        "Albuquerque",
        "Tucson",
        "Colorado Springs",
        "Louisville",
        "Miami",
        "Tulsa",
        "Wichita",
        "Omaha",
        "Virginia Beach",
        "Salt Lake City",
        "Oakland",
        "Des Moines",
        "Charleston",
        "Mesa",
        "California",
        "Texas",
        "Florida",
        "Indiana",
        "Michigan",
        "Missouri",
        "New Jersey",
        "St. Petersburg",
        "Moreno Valley",
        "Tacoma",
        "Rochester",
        "Columbus",
        "Frisco",
        "Oxnard",
        "Sioux Falls",
        "Tallahassee",
        "Virginia",
        "Louisiana",
        "Illinois",
        "Aurora",
        "Santa Rosa",
        "Lancaster",
        "Springfield",
        "Hayward",
        "Clarksville",
        "Paterson",
        "Hollywood",
        "Mississippi",
        "Rockford",
        "Fullerton",
        "West Valley City",
        "Elizabeth",
        "Kent",
        "Miramar",
        "Midland",
        "Iowa",
        "Carrollton",
        "Fargo",
        "Pearland",
        "North Dakota",
        "Thousand Oaks",
        "Allentown",
        "Colorado",
        "Clovis",
        "Hartford",
        "Connecticut",
        "Wilmington",
        "Fairfield",
        "Cambridge",
        "Billings",
        "West Palm Beach",
        "Westminster",
        "Provo",
        "Lewisville",
        "New Mexico",
        "Odessa",
        "Nevada",
        "Greeley",
        "Tyler",
        "Oregon",
        "Rio Rancho",
        "Massachusetts",
        "New Bedford",
        "Longmont",
        "Hesperia",
        "Chico",
        "Burbank",
        "Murfreesboro",
        "Kansas",
        "Idaho",
        "Pittsburgh",
        "Detroit",
        "Pennsylvania",
        "Jacksonville",
        "Indianapolis",
        "Charlotte",
        "Jefferson City",
        "Arizona",
        "Ohio",
        "North Carolina",
        "Oklahoma",
        "Denver",
        "El Paso",
        "Kentucky",
        "Nevada",
        "Milwaukee",
        "Baltimore",
        "Wisconsin",
        "New Mexico",
        "Fresno",
        "Tucson",
        "Sacramento",
        "Nebraska",
        "Omaha",
        "Raleigh",
        "Oakland",
        "Miami",
        "Minneapolis",
        "Bakersfield",
        "Tampa",
        "New Orleans",
        "Honolulu",
        "Lexington",
        "Stockton",
        "St. Louis",
        "Lubbock",
        "Buffalo",
        "Glendale",
        "Hialeah",
        "Connecticut"
    )
    var cityName: String = ""
    var cityDetails: String = ""
    var appId: String = "4db19b747b0a3369815069fb2ef8d024"
    //var appId: String = "6134f58493547c1c8a615102693ac120"
    private var cityList: MutableList<String>? = null
    private var cityDataList: MutableList<String>? = null
    private val weatherViewModel: WeatherViewModel by viewModels()
    lateinit var weatherBinding: FragmentWeatherBinding
    @Inject
    lateinit var weatherAdapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weatherBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        return weatherBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(citiesList != null) {
            cityDataList = citiesList.toMutableList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val lowercaseList = cityDataList!!.map { it.lowercase() }
                cityList = lowercaseList.toMutableList()
            }
        }
        //Location Enable Permission
        try {
            if (ContextCompat.checkSelfPermission(context as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) !==
                PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                } else {
                    ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
        //Search CityName
        weatherBinding.etAWEnterCityName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(text: CharSequence, start: Int,
                                       before: Int, count: Int) {
                cityName = text.toString().trim();
                if(StringUtils.isNotEmpty(cityName)){
                    weatherBinding.tvAWCityNameLabel.isVisible = true
                }else {
                    weatherBinding.tvAWCityNameLabel.isVisible = false
                }
            }
        })
        val timerThread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    try {
                        if (getStringData(requireActivity(), SPHelper.cityName, "")!!.trim { it <= ' ' }.length > 0) {
                            (requireActivity()).runOnUiThread {
                                if (Helper.checkInternetConnection(requireContext())) {
                                    cityName = getStringData(requireActivity(), SPHelper.cityName, "")!!.trim { it <= ' ' }
                                    passingDataToServerCall()
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        }
        timerThread.start()

        //Search Icon Click
        weatherBinding.ivAWCityNameSearchIcon.setOnClickListener({
            cityNameValidationCheck()
        })
    }
    //cityname validation check
    fun cityNameValidationCheck() {
        cityName = weatherBinding.etAWEnterCityName.getText().toString().trim().toLowerCase()
        if (!StringUtils.isNotEmpty(cityName)) {
            weatherBinding.etAWEnterCityName.requestFocus()
            weatherBinding.etAWEnterCityName.setError(getString(R.string.cityname_required))
            return
        }else if (cityName.length < 3){
            weatherBinding.etAWEnterCityName.requestFocus()
            weatherBinding.etAWEnterCityName.setError(getString(R.string.valid_citynamecharacters))
            return
        } else {
            weatherBinding.etAWEnterCityName.setError(null)
        }
        if (Helper.checkInternetConnection(requireContext())) {
            if (StringUtils.isNotEmpty(cityName)) {
                cityDetails = if (cityName.contains(",")) {
                    val cityInfo = cityName.split(",").toTypedArray()
                    cityInfo[0].trim { it <= ' ' }.lowercase(Locale.getDefault())
                } else {
                    cityName
                }
                if (cityList!!.contains(cityDetails)) {
                    SPHelper.saveStringData(requireActivity(), SPHelper.cityName, cityName);
                    passingDataToServerCall()
                } else {
                    Helper.showShortToast(requireActivity(), getString(R.string.valid_cityname))
                    weatherBinding.etAWEnterCityName.setText("")
                    weatherBinding.tvAWNoWeatherList.isVisible = true
                    weatherBinding.rvAWWeatherList.isVisible = false
                }
            }
        }
    }
    //Passing data to API call
    fun passingDataToServerCall(){
        weatherBinding.pbAWProgressDialog.visibility = View.VISIBLE
        weatherBinding.rvAWWeatherList.adapter = weatherAdapter
        //Calling Coroutines function
        weatherViewModel.getWeatherReport(cityName, appId)
        //Calling Retrofit enqueue, Callback - Optional (if you use this, disable(comment) the above line and enable(un comment) the below line
        //weatherViewModel.getWeatherReportData(cityName, appId)
        weatherViewModel.observeWeatherLiveData().observe(viewLifecycleOwner) {
            weatherAdapter.setWeatherList(it)
        }
        weatherViewModel.errorMessage.observe(viewLifecycleOwner) {
            Helper.showShortToast(context, it)
        }
        weatherViewModel.loading.observe(viewLifecycleOwner) {
            if (it){
                weatherBinding.pbAWProgressDialog.visibility = View.VISIBLE
            }else {
                weatherBinding.pbAWProgressDialog.visibility= View.GONE
            }
        }
    }
}