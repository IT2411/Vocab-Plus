package com.vocabplus.app.data.validator

import com.vocabplus.app.data.model.QuestionDto
import com.vocabplus.app.domain.model.Category
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ProductionAssetContentTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private fun loadAssetFile(fileName: String): List<QuestionDto> {
        val assetDir = File("src/main/assets/questions")
        val file = File(assetDir, fileName)
        assertTrue("Asset file '$fileName' must exist at ${file.absolutePath}", file.exists())
        val content = file.readText()
        return json.decodeFromString<List<QuestionDto>>(content)
    }

    @Test
    fun `all bundled synonyms pass strict quality requirements`() {
        val dtos = loadAssetFile("synonyms.json")
        assertTrue("Synonyms question bank must not be empty", dtos.isNotEmpty())

        val domainQuestions = dtos.map { it.toDomain() }
        domainQuestions.forEach { question ->
            assertEquals(Category.SYNONYM, question.category)
            assertTrue("ID must start with syn_", question.id.startsWith("syn_"))
        }

        val validationResult = ContentValidator.validateQuestions(domainQuestions)
        if (validationResult is ContentValidator.ValidationResult.Invalid) {
            throw AssertionError("Synonyms validation failed:\n" + validationResult.errors.joinToString("\n"))
        }
    }

    @Test
    fun `all bundled antonyms pass strict quality requirements`() {
        val dtos = loadAssetFile("antonyms.json")
        assertTrue("Antonyms question bank must not be empty", dtos.isNotEmpty())

        val domainQuestions = dtos.map { it.toDomain() }
        domainQuestions.forEach { question ->
            assertEquals(Category.ANTONYM, question.category)
            assertTrue("ID must start with ant_", question.id.startsWith("ant_"))
        }

        val validationResult = ContentValidator.validateQuestions(domainQuestions)
        if (validationResult is ContentValidator.ValidationResult.Invalid) {
            throw AssertionError("Antonyms validation failed:\n" + validationResult.errors.joinToString("\n"))
        }
    }

    @Test
    fun `all bundled idioms pass strict quality requirements`() {
        val dtos = loadAssetFile("idioms.json")
        assertTrue("Idioms question bank must not be empty", dtos.isNotEmpty())

        val domainQuestions = dtos.map { it.toDomain() }
        domainQuestions.forEach { question ->
            assertEquals(Category.IDIOM, question.category)
            assertTrue("ID must start with idm_", question.id.startsWith("idm_"))
        }

        val validationResult = ContentValidator.validateQuestions(domainQuestions)
        if (validationResult is ContentValidator.ValidationResult.Invalid) {
            throw AssertionError("Idioms validation failed:\n" + validationResult.errors.joinToString("\n"))
        }
    }

    @Test
    fun `entire combined question bank has completely unique IDs and valid references`() {
        val syn = loadAssetFile("synonyms.json").map { it.toDomain() }
        val ant = loadAssetFile("antonyms.json").map { it.toDomain() }
        val idm = loadAssetFile("idioms.json").map { it.toDomain() }

        val combined = syn + ant + idm
        val result = ContentValidator.validateQuestions(combined)

        assertTrue(
            "Combined content pipeline validation failed",
            result is ContentValidator.ValidationResult.Valid
        )
    }
}