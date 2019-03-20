package com.yoox.sylvia.rx

import com.yoox.net.Request
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class RXTest {

    @Test
    fun shouldExecuteSingleWithSuccess() {

        val request: Request<String> = object : Request<String> {
            override suspend fun execute(): String {
                return "expected"
            }
        }

        val single: Single<String> = request.asSingle()

        single.subscribe(
            { value: String ->
                assertEquals("expected", value)
            },
            {
                fail()
            }
        )
    }

    @Test
    fun shouldExecuteSingleWithError() {

        val request: Request<String> = object : Request<String> {
            override suspend fun execute(): String {
                throw Exception("error message")
            }
        }

        val single: Single<String> = request.asSingle()

        single.subscribe(
            {
                fail()
            },
            { t: Throwable ->
                assertEquals("error message", t.message)
            }
        )
    }
}