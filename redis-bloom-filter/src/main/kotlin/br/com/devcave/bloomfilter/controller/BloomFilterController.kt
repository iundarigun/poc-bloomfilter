package br.com.devcave.bloomfilter.controller

import br.com.devcave.bloomfilter.domain.ValidationResponse
import br.com.devcave.bloomfilter.service.BloomFilterService
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("bloom-filter")
class BloomFilterController(
    private val bloomFilterService: BloomFilterService
) {

    @GetMapping
    fun verifyEntry(@RequestParam entry: String): HttpEntity<ValidationResponse> {
        val response = bloomFilterService.verifyEntry(entry)
        return ResponseEntity.ok(response)
    }

    @PostMapping("database")
    fun loadFromDatabase(): HttpEntity<Any?> {
        bloomFilterService.loadFromDatabase()
        return ResponseEntity.ok().build()
    }

    @PutMapping
    fun addEntry(@RequestBody entry: String): HttpEntity<Any?> {
        bloomFilterService.addEntry(entry)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping
    fun deleteEntry(@RequestBody entry: String): HttpEntity<Any?> {
        bloomFilterService.deleteEntry(entry)
        return ResponseEntity.ok().build()
    }
}