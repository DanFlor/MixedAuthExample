package com.obidan.mixedauthexample

import android.app.Application
import com.obidan.mixedauthexample.injection.AppComponent
import com.obidan.mixedauthexample.injection.AppModule
import com.obidan.mixedauthexample.injection.NetComponent
import com.obidan.mixedauthexample.injection.NetModule

class MixedAuthExampleApplication: Application() {

    lateinit var appComponent: AppComponent
        private set
    lateinit var netComponent: NetComponent
        private set

    override fun onCreate() {
        super.onCreate()

        this.appComponent = initAppComponent()
        this.netComponent = initNetComponent()
    }

    private fun initAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    private fun initNetComponent(): NetComponent {
       return appComponent.plus(NetModule())
    }

    fun forcedLogin() {
        // Force us off to the login page with nothing behind it.
    }
    companion object {
        lateinit var INSTANCE : MixedAuthExampleApplication
            private set
    }

}