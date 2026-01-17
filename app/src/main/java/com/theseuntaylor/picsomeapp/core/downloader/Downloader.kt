package com.theseuntaylor.picsomeapp.core.downloader

interface Downloader {
    fun downloadFile(url: String): Long
}