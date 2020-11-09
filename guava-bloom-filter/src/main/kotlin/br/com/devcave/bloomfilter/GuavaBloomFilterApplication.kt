package br.com.devcave.bloomfilter

import br.com.devcave.bloomfilter.configuration.BloomFilterProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(value = [BloomFilterProperties::class])
class GuavaBloomFilterApplication

fun main(args: Array<String>) {
	runApplication<GuavaBloomFilterApplication>(*args)
}
