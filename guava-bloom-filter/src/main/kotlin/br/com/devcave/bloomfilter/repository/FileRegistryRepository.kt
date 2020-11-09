package br.com.devcave.bloomfilter.repository

import br.com.devcave.bloomfilter.domain.entity.FileRegistry
import org.springframework.data.repository.CrudRepository

interface FileRegistryRepository : CrudRepository<FileRegistry, Long> {
    fun findFirstByOrderByCreatedAtDesc(): FileRegistry?
}