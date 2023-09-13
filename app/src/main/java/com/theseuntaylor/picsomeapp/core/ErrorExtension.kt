package com.theseuntaylor.picsomeapp.core

import android.accounts.NetworkErrorException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

fun Throwable.transformException(): Throwable {
    return when (this) {
        is SocketTimeoutException, is SSLException,
        is NetworkErrorException, is ConnectException, is UnknownHostException -> Throwable(
            "There seems to be no network connection"
        )

        else -> this

    }
}

