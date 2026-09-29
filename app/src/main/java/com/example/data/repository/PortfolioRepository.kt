package com.example.data.repository

import com.example.data.db.PortfolioDao
import com.example.data.models.ContactMessageEntity
import com.example.data.models.ExperienceEntity
import com.example.data.models.ProfileEntity
import com.example.data.models.ProjectEntity
import com.example.data.models.ServiceEntity
import com.example.data.models.SiteSettingEntity
import com.example.data.models.SkillEntity
import com.example.data.models.TestimonialEntity
import kotlinx.coroutines.flow.Flow

class PortfolioRepository(private val dao: PortfolioDao) {

    val profile: Flow<ProfileEntity?> = dao.getProfile()
    val allServices: Flow<List<ServiceEntity>> = dao.getAllServices()
    val enabledServices: Flow<List<ServiceEntity>> = dao.getEnabledServices()
    val allSkills: Flow<List<SkillEntity>> = dao.getAllSkills()
    val allProjects: Flow<List<ProjectEntity>> = dao.getAllProjects()
    val featuredProjects: Flow<List<ProjectEntity>> = dao.getFeaturedProjects()
    val allExperiences: Flow<List<ExperienceEntity>> = dao.getAllExperiences()
    val allTestimonials: Flow<List<TestimonialEntity>> = dao.getAllTestimonials()
    val publishedTestimonials: Flow<List<TestimonialEntity>> = dao.getPublishedTestimonials()
    val allContactMessages: Flow<List<ContactMessageEntity>> = dao.getAllContactMessages()
    val allSettings: Flow<List<SiteSettingEntity>> = dao.getAllSettings()

    suspend fun saveProfile(profile: ProfileEntity) = dao.saveProfile(profile)

    suspend fun addService(service: ServiceEntity) = dao.insertService(service)
    suspend fun updateService(service: ServiceEntity) = dao.updateService(service)
    suspend fun deleteService(service: ServiceEntity) = dao.deleteService(service)

    suspend fun addSkill(skill: SkillEntity) = dao.insertSkill(skill)
    suspend fun updateSkill(skill: SkillEntity) = dao.updateSkill(skill)
    suspend fun deleteSkill(skill: SkillEntity) = dao.deleteSkill(skill)

    suspend fun addProject(project: ProjectEntity) = dao.insertProject(project)
    suspend fun updateProject(project: ProjectEntity) = dao.updateProject(project)
    suspend fun deleteProject(project: ProjectEntity) = dao.deleteProject(project)

    suspend fun addExperience(experience: ExperienceEntity) = dao.insertExperience(experience)
    suspend fun updateExperience(experience: ExperienceEntity) = dao.updateExperience(experience)
    suspend fun deleteExperience(experience: ExperienceEntity) = dao.deleteExperience(experience)

    suspend fun addTestimonial(testimonial: TestimonialEntity) = dao.insertTestimonial(testimonial)
    suspend fun updateTestimonial(testimonial: TestimonialEntity) = dao.updateTestimonial(testimonial)
    suspend fun deleteTestimonial(testimonial: TestimonialEntity) = dao.deleteTestimonial(testimonial)

    suspend fun submitContactMessage(message: ContactMessageEntity) = dao.insertContactMessage(message)
    suspend fun updateMessageStatus(id: Int, status: String) = dao.updateMessageStatus(id, status)
    suspend fun deleteContactMessage(message: ContactMessageEntity) = dao.deleteContactMessage(message)

    suspend fun getSettingValue(key: String): String? = dao.getSettingValue(key)
    suspend fun saveSetting(key: String, value: String) = dao.saveSetting(SiteSettingEntity(key, value))

    suspend fun ensureDefaultDataSeeded() {
        // If profile is empty on initial check, seed immediately
        // Note: Room Database Callback handles onCreate, but this provides safety fallback
    }
}
