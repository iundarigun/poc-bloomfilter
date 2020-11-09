package br.com.devcave.bloomfilter.repository

import br.com.devcave.bloomfilter.domain.entity.BloomFilterEntry
import br.com.devcave.bloomfilter.domain.entity.FileRegistry
import org.springframework.data.repository.CrudRepository

interface BloomFilterEntryRepository: CrudRepository<BloomFilterEntry, Long> {
    fun existsByEntry(entry: String): Boolean
    fun findByEntry(entry: String): BloomFilterEntry?
}