package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Talha Mahmood",
    val brandTitle: String = "Digital Creator & Technology Professional",
    val profileImageUrl: String = "https://i.postimg.cc/ydhLzpzc/profile.jpg",
    val heroHeadline: String = "Turning Ideas Into Powerful Digital Experiences.",
    val heroSubheadline: String = "I create modern websites, professional brand identities, and digital solutions that help businesses build trust and grow online.",
    val shortBio: String = "Professional website creator and digital services provider helping individuals, students, startups, and businesses build a strong online presence.",
    val longBio: String = "I'm Talha Mahmood, a website creator and digital services professional focused on building attractive, functional and user-friendly digital experiences.\n\nI work with individuals, startups and businesses that need a professional online presence. My services combine website creation, branding, SEO, digital marketing and accurate data services.\n\nMy goal is simple: understand the client's needs, create a professional solution, and deliver work that is easy to use, visually impressive and built around the client's objectives.",
    val email: String = "talhamahmood1055@gmail.com",
    val phone: String = "03255691055",
    val whatsapp: String = "03255691055",
    val location: String = "Pakistan / Remote Worldwide",
    val availabilityStatus: String = "Available for New Projects",
    val education: String = "Technology & Computer Science Background",
    val githubUrl: String = "https://github.com",
    val linkedinUrl: String = "https://linkedin.com",
    val cvUrl: String = ""
)

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val features: String, // Comma or newline separated
    val iconKey: String, // e.g. "web", "brush", "data", "seo", "marketing", "code"
    val category: String = "Core",
    val orderIndex: Int = 0,
    val isEnabled: Boolean = true
)

@Entity(tableName = "skills")
data class SkillEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // "Development", "Design", "Marketing", "Data"
    val proficiencyPercentage: Int, // Realistic percentage, e.g. 85-95%
    val iconKey: String = "code",
    val orderIndex: Int = 0
)

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Websites", "Design", "SEO", "Marketing", "Other"
    val description: String,
    val technologies: String, // Comma separated e.g. "React, TypeScript, Tailwind, Supabase"
    val projectUrl: String = "",
    val githubUrl: String = "",
    val clientName: String = "",
    val dateCompleted: String = "2026",
    val isFeatured: Boolean = false,
    val imageUrl: String = "",
    val orderIndex: Int = 0
)

@Entity(tableName = "experience")
data class ExperienceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val position: String,
    val organization: String,
    val period: String,
    val description: String,
    val skillsUsed: String,
    val orderIndex: Int = 0
)

@Entity(tableName = "testimonials")
data class TestimonialEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientName: String,
    val clientRole: String,
    val review: String,
    val projectName: String,
    val rating: Int = 5,
    val dateAdded: String = "2026",
    val isPublished: Boolean = true
)

@Entity(tableName = "contact_messages")
data class ContactMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val company: String,
    val serviceRequired: String,
    val budget: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "New", // "New", "Contacted", "In Progress", "Completed", "Archived"
    val adminNotes: String = ""
)

@Entity(tableName = "site_settings")
data class SiteSettingEntity(
    @PrimaryKey val key: String,
    val value: String
)
