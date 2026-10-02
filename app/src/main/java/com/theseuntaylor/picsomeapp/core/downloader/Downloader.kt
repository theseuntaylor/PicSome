package com.theseuntaylor.picsomeapp.core.downloader

interface Downloader {
    fun downloadFile(url: String, fileName: String, title: String): Long
}