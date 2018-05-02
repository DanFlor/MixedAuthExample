package com.obidan.mixedauthexample

import android.app.Fragment
import android.os.Bundle
import com.obidan.mixedauthexample.api.MixedAuthAPI
import com.obidan.mixedauthexample.injection.Injector

abstract class MixedAuthExampleFragment: Fragment() {

    lateinit var app: MixedAuthExampleApplication
    lateinit var api: MixedAuthAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.getAppComponent().inject(this)
        Injector.getNetComponent().inject(this)
    }
}