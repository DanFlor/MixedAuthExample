package com.obidan.mixedauthexample.extension

import com.obidan.mixedauthexample.Constants

fun String.hasBearerAuthTokenPrefix() : Boolean {
    return this.startsWith(Constants.httpHeaderBearerTokenPrefix)
}

fun String.withBearerAuthTokenPrefix() : String {
    return when (this.hasBearerAuthTokenPrefix()) {
        false -> Constants.httpHeaderBearerTokenPrefix + this
        true -> this
    }
}

fun String.trimmingBearerAuthTokenPrefix(): String {
    var trimmed = this
    // while it starts with the prefix:
    while (trimmed.hasBearerAuthTokenPrefix()) {
        // trim off a prefix!
        trimmed = trimmed.substring(Constants.httpHeaderBearerTokenPrefix.length)
    }
    return trimmed
}

