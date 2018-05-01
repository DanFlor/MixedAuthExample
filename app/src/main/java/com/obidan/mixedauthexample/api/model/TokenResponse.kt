package com.obidan.mixedauthexample.api.model

/**
 * Created by obidan on 4/3/18.
 */
data class TokenResponse(
        val access_token: String,
        val token_type: String,
        val refresh_token: String,
        val scope: String,
        val user_id: Int,
        val location_id: Int,
        val jti: String
)