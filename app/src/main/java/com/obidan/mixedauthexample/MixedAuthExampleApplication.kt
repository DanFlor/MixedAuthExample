package com.obidan.mixedauthexample

import android.app.Application
import com.obidan.mixedauthexample.injection.AppComponent
import com.obidan.mixedauthexample.injection.NetComponent

class MixedAuthExampleApplication: Application() {

    lateinit var appComponent: AppComponent
        private set
    lateinit var netComponent: NetComponent
        private set

    companion object {
        lateinit var INSTANCE : MixedAuthExampleApplication
            private set
    }

}