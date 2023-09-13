package com.theseuntaylor.picsomeapp.core

import org.junit.Test
import javax.net.ssl.SSLException

class ErrorExtensionTest {


    @Test
    fun `when a throwable is gotten, a transformed throwable is returned`() {
        // given
        val throwable = SSLException("")
        val expectedThrowable = Throwable(message = "There seems to be no network connection")

        // when
        val transformedException = throwable.transformException()
        // then
        assert(transformedException.message == expectedThrowable.message)

    }

}