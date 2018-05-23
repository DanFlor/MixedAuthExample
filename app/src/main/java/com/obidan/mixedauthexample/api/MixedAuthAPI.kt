package com.obidan.mixedauthexample.api

import com.obidan.mixedauthexample.BuildConfig
import com.obidan.mixedauthexample.Constants
import com.obidan.mixedauthexample.api.delegate.MixedAuthCredentialsDelegate
import com.obidan.mixedauthexample.api.model.TokenResponse
import io.reactivex.Single
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MixedAuthAPI {

    // A legacy endpoint that is not wired up to Oauth yet:
    @GET(BuildConfig.API_SUB_URL + "a_basic_auth_endpoint/")
    fun getABasicAuthEndpoint(
            @Header(Constants.httpHeaderAuthorization) basicCredentials: String =
                    credentialsDelegate?.userBasicAuthCredentials() ?: ""
    ): Single<Void>

    // An endpoint that is secured with the user's login token
    @GET(BuildConfig.OAUTH_SUB_URL + "an_oauth_endpoint/")
    fun getAnOauthEndpoint(
            @Header(Constants.httpHeaderAuthorization) bearerToken: String =
                    credentialsDelegate?.userOauthBearerToken() ?: ""
    ): Single<Void>


    @FormUrlEncoded
    @POST(BuildConfig.OAUTH_SUB_URL + "oauth/token")
    fun login(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("grant_type") grant_type: String = "password",
            @Field("scope") scope: String = BuildConfig.OAUTH_SCOPE,
            @Header(Constants.httpHeaderAuthorization) clientCredentials: String =
                credentialsDelegate?.clientBasicAuthCredentials() ?: "",
            @Header("Cache-Control") cacheControlHeader: String = "no-cache"
    ): Single<TokenResponse>  // Use asynchronously from the main thread for UI Login

    @FormUrlEncoded
    @POST(BuildConfig.OAUTH_SUB_URL + "oauth/token")
    fun refreshLogin(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("grant_type") grant_type: String = "password",
            @Field("scope") scope: String = BuildConfig.OAUTH_SCOPE,
            @Header(Constants.httpHeaderAuthorization) clientCredentials: String =
                    credentialsDelegate?.clientBasicAuthCredentials() ?: "",
            @Header("Cache-Control") cacheControlHeader: String = "no-cache"
    ): Single<TokenResponse>  // Use synchronously from a background thread for token refresh

    @FormUrlEncoded
    @POST(BuildConfig.OAUTH_SUB_URL + "oauth/token")
    fun refreshTokens(
            @Field("refresh_token") refreshToken: String =
                    credentialsDelegate?.userOauthRefreshToken() ?: "",
            @Field("grant_type") grant_type: String = "refresh_token",
            @Header(Constants.httpHeaderAuthorization) clientCredentials: String =
                    credentialsDelegate?.clientBasicAuthCredentials() ?: "",
            @Header("Cache-Control") cacheControlHeader: String = "no-cache"
    ): Call<TokenResponse> // Use synchronously from a background thread for token refresh

    companion object Factory {

        // A static to hold the delegate, assigned when the factory creates the retrofit:
        var credentialsDelegate: MixedAuthCredentialsDelegate? = null

        // A static factory method to instantiate the retrofit:
        fun create(
                credentialsDelegate: MixedAuthCredentialsDelegate,
                gsonConverterFactory: GsonConverterFactory,
                httpLoggingInterceptor: HttpLoggingInterceptor,
                oauthRefreshAuthenticator: OauthRefreshAuthenticator,
                cookieJar: CookieJar
        ): MixedAuthAPI {

            // This is the reference the implementation will use for its credentials:
            Factory.credentialsDelegate = credentialsDelegate

            val client = OkHttpClient().newBuilder()
                    .connectTimeout(BuildConfig.NETWORK_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(BuildConfig.NETWORK_READ_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(httpLoggingInterceptor)
                    // Add the authenticator to refresh stale bearer tokens:
                    .authenticator(oauthRefreshAuthenticator)
                    .cookieJar(cookieJar)
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            return retrofit.create(MixedAuthAPI::class.java)
        }
    }
}