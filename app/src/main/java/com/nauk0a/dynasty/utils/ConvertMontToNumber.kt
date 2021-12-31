package com.nauk0a.dynasty.utils


fun convertnumtocharmonths(m: String): Int {
    var charname = 1
    if (m == "January") {
        charname = 1
    }
    if (m == "February") {
        charname = 2
    }
    if (m == "March") {
        charname = 3
    }
    if (m == "April") {
        charname = 4
    }
    if (m == "May") {
        charname = 5
    }
    if (m == "June") {
        charname = 6
    }
    if (m == "July") {
        charname = 7
    }
    if (m == "August") {
        charname = 8
    }
    if (m == "September") {
        charname = 9
    }
    if (m == "October") {
        charname = 10
    }
    if (m == "November") {
        charname = 11
    }
    if (m == "December") {
        charname = 12
    }
    return charname
}