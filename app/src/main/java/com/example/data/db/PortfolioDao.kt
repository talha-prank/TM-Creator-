package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.ContactMessageEntity
import com.example.data.models.ExperienceEntity
import com.example.data.models.ProfileEntity
import com.example.data.models.ProjectEntity
import com.example.data.models.ServiceEntity
import com.example.data.models.SiteSettingEntity
import com.example.data.models.SkillEntity
import com.example.data.models.TestimonialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {

    // --- Profile ---
    @Query("SELECT * FROM profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<ProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: ProfileEntity)

    // --- Services ---
    @Query("SELECT * FROM services ORDER BY orderIndex ASC, id ASC")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE isEnabled = 1 ORDER BY orderIndex ASC, id ASC")
    fun getEnabledServices(): Flow<List<ServiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity): Long

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Delete
    suspend fun deleteService(service: ServiceEntity)

    // --- Skills ---
    @Query("SELECT * FROM skills ORDER BY orderIndex ASC, id ASC")
    fun getAllSkills(): Flow<List<SkillEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkill(skill: SkillEntity): Long

    @Update
    suspend fun updateSkill(skill: SkillEntity)

    @Delete
    suspend fun deleteSkill(skill: SkillEntity)

    // --- Projects ---
    @Query("SELECT * FROM projects ORDER BY orderIndex ASC, id DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE isFeatured = 1 ORDER BY orderIndex ASC, id DESC")
    fun getFeaturedProjects(): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    // --- Experience ---
    @Query("SELECT * FROM experience ORDER BY orderIndex ASC, id DESC")
    fun getAllExperiences(): Flow<List<ExperienceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperience(experience: ExperienceEntity): Long

    @Update
    suspend fun updateExperience(experience: ExperienceEntity)

    @Delete
    suspend fun deleteExperience(experience: ExperienceEntity)

    // --- Testimonials ---
    @Query("SELECT * FROM testimonials ORDER BY id DESC")
    fun getAllTestimonials(): Flow<List<TestimonialEntity>>

    @Query("SELECT * FROM testimonials WHERE isPublished = 1 ORDER BY id DESC")
    fun getPublishedTestimonials(): Flow<List<TestimonialEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestimonial(testimonial: TestimonialEntity): Long

    @Update
    suspend fun updateTestimonial(testimonial: TestimonialEntity)

    @Delete
    suspend fun deleteTestimonial(testimonial: TestimonialEntity)

    // --- Contact Messages / Leads CRM ---
    @Query("SELECT * FROM contact_messages ORDER BY timestamp DESC")
    fun getAllContactMessages(): Flow<List<ContactMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactMessage(message: ContactMessageEntity): Long

    @Query("UPDATE contact_messages SET status = :status WHERE id = :id")
    suspend fun updateMessageStatus(id: Int, status: String)

    @Delete
    suspend fun deleteContactMessage(message: ContactMessageEntity)

    // --- Site Settings ---
    @Query("SELECT * FROM site_settings")
    fun getAllSettings(): Flow<List<SiteSettingEntity>>

    @Query("SELECT value FROM site_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSettingValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: SiteSettingEntity)
}
