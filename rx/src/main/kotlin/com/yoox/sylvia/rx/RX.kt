package com.yoox.sylvia.rx

import com.yoox.net.Request
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.rx2.rxSingle

fun <T : Any> Request<T>.asSingle(): Single<T> =
    GlobalScope.rxSingle(Dispatchers.IO) {
        execute()
    }