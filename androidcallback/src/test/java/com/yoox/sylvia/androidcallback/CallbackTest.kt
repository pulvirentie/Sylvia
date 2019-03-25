package com.yoox.sylvia.androidcallback

import com.yoox.net.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CallbackTest {

    @Test
    fun shouldExecuteWithCallbackWithSuccess() {

        val sut = object : Request<String> {
            override suspend fun execute(): String {
                return "expected"
            }
        }

        sut.execute(object : Callback<String> {
            override fun onComplete(result: String) {
                assertEquals("expected", result)
            }

            override fun onError(t: Throwable) {
                fail()
            }
        })
    }

    @Test
    fun shouldExecuteWithCallbackWithError() {

        val sut = object : Request<String> {
            override suspend fun execute(): String {
                throw Exception("error message")
            }
        }

        sut.execute(object : Callback<String> {
            override fun onComplete(result: String) {
                fail()
            }

            override fun onError(t: Throwable) {
                assertEquals("error message", t.message)
            }
        })
    }

    @Test
    fun shouldExecuteWithSuccessConsumer() {

        val sut = object : Request<String> {
            override suspend fun execute(): String {
                return "expected"
            }
        }

        sut.execute(
            onComplete = object : Consumer<String> {
                override fun accept(value: String) {
                    assertEquals("expected", value)
                }
            },
            onError = object : Consumer<Throwable> {
                override fun accept(value: Throwable) {
                    fail()
                }
            }
        )
    }

    @Test
    fun shouldExecuteWithErrorConsumer() {

        val sut = object : Request<String> {
            override suspend fun execute(): String {
                throw Exception("error message")
            }
        }

        sut.execute(
            onComplete = object : Consumer<String> {
                override fun accept(value: String) {
                    fail()
                }
            },
            onError = object : Consumer<Throwable> {
                override fun accept(value: Throwable) {
                    assertEquals("error message", value.message)
                }
            }
        )
    }
}