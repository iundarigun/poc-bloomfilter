package br.com.devcave.bloomfilter.service

import br.com.devcave.bloomfilter.configuration.BloomFilterProperties
import br.com.devcave.bloomfilter.domain.ValidationResponse
import br.com.devcave.bloomfilter.domain.entity.BloomFilterEntry
import br.com.devcave.bloomfilter.repository.BloomFilterEntryRepository
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BloomFilterService(
    private val redissonClient: RedissonClient,
    private val bloomFilterEntryRepository: BloomFilterEntryRepository,
    private val bloomFilterProperties: BloomFilterProperties
) {

    private var bloomFilter = redissonClient.getBloomFilter<String>("redis-bloom-filter").also {
        it.tryInit(bloomFilterProperties.expectedNumberOfEntries, bloomFilterProperties.falsePositivePercent)
    }

    init {
        loadFromDatabase()
    }

    @Transactional
    fun addEntry(entry: String) {
        bloomFilter.add(entry).also {
            runCatching {
                bloomFilterEntryRepository.save(
                    BloomFilterEntry(entry = entry)
                )
            }
        }
    }

    @Transactional(readOnly = true)
    fun verifyEntry(entry: String): ValidationResponse {
        val bloomResult = bloomFilter.contains(entry)
        val validationResult = if (bloomResult) {
            bloomFilterEntryRepository.existsByEntry(entry)
        } else false

        return ValidationResponse(
            validation = validationResult,
            falsePositive = bloomResult && validationResult.not()
        )
    }

    @Transactional(readOnly = true)
    fun loadFromDatabase() {
        bloomFilter.delete()
        bloomFilterEntryRepository.findAll().forEach {
            bloomFilter.add(it.entry)
        }
    }

    @Transactional
    fun deleteEntry(entry: String) {
        bloomFilterEntryRepository.findByEntry(entry)?.let {
            bloomFilterEntryRepository.delete(it)
        }
    }
}