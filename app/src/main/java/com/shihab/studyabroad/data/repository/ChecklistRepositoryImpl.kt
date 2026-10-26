package com.shihab.studyabroad.data.repository

import com.shihab.studyabroad.data.local.dao.ChecklistProgressDao
import com.shihab.studyabroad.data.local.entity.ChecklistProgressEntity
import com.shihab.studyabroad.domain.model.ChecklistItem
import com.shihab.studyabroad.domain.model.Track
import com.shihab.studyabroad.domain.repository.ChecklistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChecklistRepositoryImpl @Inject constructor(private val checklistProgressDao: ChecklistProgressDao, private val clock: Clock) :
    ChecklistRepository {

    override fun getChecklistItems(track: Track?): Flow<List<ChecklistItem>> {
        val baseItems = getItemsForTrack(track)
        return checklistProgressDao.observeAllProgress().map { progressList ->
            val progressMap = progressList.associate { it.itemId to it.isChecked }
            baseItems.map { item ->
                val isCompleted = progressMap[item.id] ?: item.isCompleted
                item.copy(isCompleted = isCompleted)
            }
        }
    }

    override suspend fun toggleItemCompleted(id: String) {
        val currentProgress = checklistProgressDao.getProgress(id)
        val newChecked = if (currentProgress != null) !currentProgress.isChecked else true
        checklistProgressDao.insertOrUpdateProgress(
            ChecklistProgressEntity(
                itemId = id,
                isChecked = newChecked,
                updatedAt = clock.now().toEpochMilliseconds(),
            ),
        )
    }

    private fun getItemsForTrack(track: Track?): List<ChecklistItem> {
        val allItems = listOf(
            ChecklistItem(
                "chk_passport",
                "Passport (valid 2+ years)",
                "Ensure passport has at least 2 years validity remaining.",
                false,
                "Identity",
            ),
            ChecklistItem(
                "chk_ssc_hsc",
                "SSC and HSC Certificates & Marksheets",
                "Attest academic certificates from Education Board.",
                false,
                "Academics",
            ),
            ChecklistItem(
                "chk_degree",
                "BSc / MSc Certificate & Transcript",
                "Official transcript and main degree certificate from University.",
                false,
                "Academics",
            ),
            ChecklistItem(
                "chk_english",
                "English Language Proficiency (IELTS/TOEFL)",
                "Target overall band 6.5 - 7.0 for scholarship eligibility.",
                false,
                "Language",
            ),
            ChecklistItem(
                "chk_cv",
                "CV (Europass / Standard Format)",
                "Prepare updated CV highlighting academics, projects, and work.",
                false,
                "Documents",
            ),
            ChecklistItem(
                "chk_sop",
                "Statement of Purpose (SOP)",
                "Draft 1-2 page SOP tailored to target track and goals.",
                false,
                "Documents",
            ),
            ChecklistItem(
                "chk_lor",
                "Academic Recommendation Letters (2 LORs)",
                "Obtain letters from university professors or department head.",
                false,
                "Documents",
            ),
            ChecklistItem(
                "chk_exp_letter",
                "Employer Reference & Experience Letter",
                "Work experience certificate detailing job role and tenure.",
                false,
                "Experience",
            ),
            ChecklistItem(
                "chk_salary_tax",
                "Salary Certificate & Tax Return",
                "Proof of employment income and tax filing documents.",
                false,
                "Financials",
            ),
            ChecklistItem(
                "chk_research_prop",
                "Research Proposal & Supervisor Contact",
                "Detailed proposal and email communication with potential professor.",
                false,
                "Research",
            ),
            ChecklistItem(
                "chk_gre_gmat",
                "GRE / GMAT Score Report",
                "Official score report if required by department or university.",
                false,
                "Tests",
            ),
            ChecklistItem(
                "chk_police_medical",
                "Police Clearance & Medical Report",
                "Police clearance certificate from Bangladesh Police and health check.",
                false,
                "Clearance",
            ),
            ChecklistItem(
                "chk_bank_stmt",
                "Bank Statement & Sponsor Proof",
                "Bank solvent certificate and sponsor affidavit for visa.",
                false,
                "Financials",
            ),
            ChecklistItem(
                "chk_attestation",
                "Government Attestation (MoE, MoFA)",
                "Attestation steps through Education Ministry and Ministry of Foreign Affairs.",
                false,
                "Legalization",
            ),
        )

        return allItems.filter { item ->
            when (track) {
                Track.FRESHER -> item.id !in setOf("chk_degree", "chk_exp_letter", "chk_salary_tax", "chk_research_prop")
                Track.GRADUATE -> item.id !in setOf("chk_exp_letter", "chk_salary_tax", "chk_research_prop")
                Track.PROFESSIONAL -> item.id !in setOf("chk_research_prop")
                Track.RESEARCHER -> true
                null -> true
            }
        }
    }
}
