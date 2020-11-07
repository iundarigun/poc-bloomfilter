package br.com.devcave.bloomfilter.service

import br.com.devcave.bloomfilter.configuration.BloomFilterProperties
import br.com.devcave.bloomfilter.domain.ValidationResponse
import br.com.devcave.bloomfilter.domain.entity.BloomFilterEntry
import br.com.devcave.bloomfilter.domain.entity.FileRegistry
import br.com.devcave.bloomfilter.repository.BloomFilterEntryRepository
import br.com.devcave.bloomfilter.repository.FileRegistryRepository
import com.google.common.hash.BloomFilter
import com.google.common.hash.Funnels
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.util.UUID

@Service
class BloomFilterService(
    private val bloomFilterEntryRepository: BloomFilterEntryRepository,
    private val fileRegistryRepository: FileRegistryRepository,
    private val bloomFilterProperties: BloomFilterProperties
) {

    private var bloomFilter = BloomFilter.create(
        Funnels.stringFunnel(Charset.defaultCharset()),
        bloomFilterProperties.expectedNumberOfEntries,
        bloomFilterProperties.falsePositivePercent
    )

    init {
        loadFromDatabase()
    }

    @Transactional
    fun addEntry(entry: String) {
        bloomFilter.put(entry).also {
            runCatching {
                bloomFilterEntryRepository.save(
                    BloomFilterEntry(entry = entry)
                )
            }
        }
    }

    @Transactional
    fun saveOnDisk() {
        val fileName = UUID.randomUUID().toString() + ".txt"
        FileOutputStream(fileName).let {
            bloomFilter.writeTo(it)
            it.close()
        }
        fileRegistryRepository.save(FileRegistry(fileName = fileName))
    }

    @Transactional(readOnly = true)
    fun loadFromDisk() {
        val filename = fileRegistryRepository.findFirstByOrderByCreatedAtDesc()?.fileName
        filename?.let {
            bloomFilter = BloomFilter.readFrom(
                FileInputStream(it),
                Funnels.stringFunnel(Charset.defaultCharset())
            )
        }
    }

    @Transactional(readOnly = true)
    fun verifyEntry(entry: String): ValidationResponse {
        val bloomResult = bloomFilter.mightContain(entry)
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
        bloomFilter = BloomFilter.create(
            Funnels.stringFunnel(Charset.defaultCharset()),
            bloomFilterProperties.expectedNumberOfEntries,
            bloomFilterProperties.falsePositivePercent
        ).also { bl ->
            bloomFilterEntryRepository.findAll().forEach {
                bl.put(it.entry)
            }
        }
    }

    @Transactional
    fun deleteEntry(entry: String) {
        bloomFilterEntryRepository.findByEntry(entry)?.let {
            bloomFilterEntryRepository.delete(it)
        }
    }
}