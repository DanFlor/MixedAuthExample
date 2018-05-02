package com.obidan.mixedauthexample

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.obidan.mixedauthexample.api.MixedAuthAPI
import com.obidan.mixedauthexample.injection.Injector
import javax.inject.Inject

abstract class MixedAuthExampleActivity: AppCompatActivity() {

    @Inject
    lateinit var app: MixedAuthExampleApplication
    @Inject
    lateinit var api: MixedAuthAPI

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Injector.getNetComponent().inject(this)
    }

}