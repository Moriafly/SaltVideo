package com.salt.video.util

import projekt.cloud.piece.c2.pinyin.C2Pinyin.pinyin

/**
 * 转拼音（自动大写）
 */
val String.pinyinString: String
    get() {
        val source = this.replace(
            "长" to "CHANG",
            "给" to "GEI",
            "藏" to "CANG"
        )
        return source.pinyin.uppercase()
    }

private fun String.replace(vararg replacements: Pair<String, String>): String {
    var result = this
    replacements.forEach { (l, r) -> result = result.replace(l, r) }
    return result
}