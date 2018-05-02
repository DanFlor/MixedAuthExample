package com.obidan.mixedauthexample

import android.app.Application
import com.obidan.mixedauthexample.injection.AppModule
import com.obidan.mixedauthexample.injection.DaggerNetComponent
import com.obidan.mixedauthexample.injection.NetComponent
import com.obidan.mixedauthexample.injection.NetModule

class MixedAuthExampleApplication: Application() {

    lateinit var netComponent: NetComponent
        private set

    override fun onCreate() {
        super.onCreate()
        this.netComponent = initNetComponent()
    }

    private fun initNetComponent(): NetComponent {
        return DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule())
                .build()
    }

    fun returnToLogin() {
        // Force us off to the login page with nothing behind it.
    }
    companion object {
        lateinit var INSTANCE : MixedAuthExampleApplication
            private set
    }

}