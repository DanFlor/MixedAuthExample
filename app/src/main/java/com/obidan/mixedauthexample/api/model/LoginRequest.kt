package com.obidan.mixedauthexample.api.model

import com.obidan.mixedauthexample.BuildConfig

data class LoginRequest(
        val username: String,
        val password: String,
        val grant_type: String = "password",
        val scope: String = BuildConfig.OAUTH_SCOPE
)