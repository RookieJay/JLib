package pers.jay.library.utils.debug

import android.util.Log

/**
 * @Author RookieJay
 * @Time 2022/1/24 15:41
 * @Description 耗时记录
 */
object TimeTracer {

    private val TAG = TimeTracer::class.simpleName

    private val hashMap by lazy {
        hashMapOf<String, Long>()
    }

    @JvmStatic
    fun start(tag: String?) {
        val tagKey = tag ?: TAG
        val startTime = currentTime()
        hashMap[tagKey!!] = startTime
    }

    @JvmStatic
    fun end(tag: String?, minCosts: Long = 5L) {
        val tagKey = tag ?: TAG
        val endTime = currentTime()

        if (hashMap.containsKey(tagKey)) {
            val startTime = hashMap[tagKey!!]
            startTime?.let {
                val duration = endTime - startTime
                if (duration >= minCosts) {
                    Log.d(TAG, "[$tag] costs $duration millis")
                }
            } ?: Log.e(TAG, "can not find $tag key")
            hashMap.remove(tagKey)
        }
    }

    private fun currentTime() = System.currentTimeMillis()

}