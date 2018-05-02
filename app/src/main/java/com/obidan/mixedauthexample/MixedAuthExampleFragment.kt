package com.obidan.mixedauthexample

import android.app.Fragment
import android.os.Bundle
import com.obidan.mixedauthexample.api.MixedAuthAPI
import com.obidan.mixedauthexample.injection.Injector
import javax.inject.Inject

abstract class MixedAuthExampleFragment: Fragment() {

    @Inject
    lateinit var app: MixedAuthExampleApplication
    @Inject
    lateinit var api: MixedAuthAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.getNetComponent().inject(this)
    }
}