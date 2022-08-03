package com.model.airpods.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.model.airpods.util.fromBroadCast
import kotlinx.coroutines.launch

class AirpodsMainActivityViewModel : BaseViewModel() {


    fun getS(context:Context){
        viewModelScope.launch{
            context.fromBroadCast()
        }
    }
}