package com.bbdevs.app.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeUtil {

    companion object {

        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm")
        fun toHumanReadableFormat(ldt: LocalDateTime): String {
            return ldt.format(formatter)
        }

    }

}