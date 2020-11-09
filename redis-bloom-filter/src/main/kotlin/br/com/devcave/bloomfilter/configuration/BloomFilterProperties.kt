package br.com.devcave.bloomfilter.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("bloom-filter")
data class BloomFilterProperties(
    val expectedNumberOfEntries: Long,
    val falsePositivePercent: Double
)