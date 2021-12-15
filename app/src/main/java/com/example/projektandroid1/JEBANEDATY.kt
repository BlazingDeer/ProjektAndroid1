package com.example.projektandroid1

import java.util.*

class JEBANEDATY {

    //comapnion object to po to zeby te funkcje w srodku byly traktowane
    // jak static bo kotlin kurwa nie ma czegfos takiego jak static
    // gowno jebane
    companion object {
        fun getNowDateWithoutTime(): Date {
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.time
        }

        fun getNowDate(): Date {
            val calendar = Calendar.getInstance()
            return calendar.time
        }

        fun getDaysAgoDateWithoutTime(daysAgo: Int): Date {
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            calendar.add(Calendar.DAY_OF_YEAR, -daysAgo) // dzisiaj - 7 dni
            return calendar.time
        }
    }
}