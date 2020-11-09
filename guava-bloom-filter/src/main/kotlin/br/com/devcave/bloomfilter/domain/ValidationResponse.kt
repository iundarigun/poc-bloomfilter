package br.com.devcave.bloomfilter.domain

data class ValidationResponse(
    val validation: Boolean,
    val falsePositive: Boolean)
