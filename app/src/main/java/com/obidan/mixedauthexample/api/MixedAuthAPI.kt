package com.obidan.mixedauthexample.api

import com.obidan.mixedauthexample.BuildConfig
import com.obidan.mixedauthexample.Constants
import com.obidan.mixedauthexample.api.model.TokenResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface MixedAuthAPI {

    @GET(BuildConfig.API_SUB_URL + "endpointA/")
    fun getEndpointA(
            @Header(Constants.httpHeaderAuthorization) basicCredentials: String =
                    credentialsDelegate?.userBasicAuthCredentials() ?: ""
    ): Single<Void>

    @GET(BuildConfig.OAUTH_SUB_URL + "endpointB/")
    fun getEndpointB(
            @Header(Constants.httpHeaderAuthorization) bearerToken: String =
                    credentialsDelegate?.userOauthBearerToken() ?: ""
    ): Single<Void>

    @FormUrlEncoded
    @POST(BuildConfig.OAUTH_SUB_URL + "oauth/token")
    fun refreshTokens(
            @Field("refresh_token") refreshToken: String =
                    credentialsDelegate?.userOauthRefreshToken() ?: "",
            @Field("grant_type") grant_type: String = "refresh_token",
            @Header(Constants.httpHeaderAuthorization) clientCredentials: String =
                    credentialsDelegate?.clientBasicAuthCredentials() ?: ""
    ): Call<TokenResponse>

    companion object {

        // A static to hold the delegate, assigned when the factory creates the retrofit:
        var credentialsDelegate: MixedAuthCredentialsDelegate? = null

        // A static factory method to instantiate the retrofit:

    }
}