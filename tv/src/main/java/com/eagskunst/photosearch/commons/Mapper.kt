package com.eagskunst.photosearch.commons

interface Mapper<T, out R> {
    suspend fun map(value: T): R
}
