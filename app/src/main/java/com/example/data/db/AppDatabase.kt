package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.models.ContactMessageEntity
import com.example.data.models.ExperienceEntity
import com.example.data.models.ProfileEntity
import com.example.data.models.ProjectEntity
import com.example.data.models.ServiceEntity
import com.example.data.models.SiteSettingEntity
import com.example.data.models.SkillEntity
import com.example.data.models.TestimonialEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProfileEntity::class,
        ServiceEntity::class,
        SkillEntity::class,
        ProjectEntity::class,
        ExperienceEntity::class,
        TestimonialEntity::class,
        ContactMessageEntity::class,
        SiteSettingEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun portfolioDao(): PortfolioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "talha_portfolio_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.portfolioDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: PortfolioDao) {
            // Seed Profile
            dao.saveProfile(ProfileEntity())

            // Seed Services
            val services = listOf(
                ServiceEntity(
                    title = "Website Creation",
                    description = "Modern, responsive and professional websites designed around your business or personal brand.",
                    features = "Business websites, Portfolio websites, Landing pages, Responsive design, Contact forms, Database integration, Admin dashboards",
                    iconKey = "web",
                    category = "Web",
                    orderIndex = 1,
                    isEnabled = true
                ),
                ServiceEntity(
                    title = "Logo & Brand Design",
                    description = "Professional visual identities designed to make your brand memorable and recognizable.",
                    features = "Custom logo concepts, Vector assets, Typography selection, Color palette guides, High-resolution exports",
                    iconKey = "brush",
                    category = "Branding",
                    orderIndex = 2,
                    isEnabled = true
                ),
                ServiceEntity(
                    title = "Data Entry",
                    description = "Accurate and organized data entry services for businesses, organizations and individuals.",
                    features = "Spreadsheet cleanup, Database records entry, Data verification, Structured formatting, Fast turnaround",
                    iconKey = "data",
                    category = "Data",
                    orderIndex = 3,
                    isEnabled = true
                ),
                ServiceEntity(
                    title = "SEO (Search Engine Optimization)",
                    description = "Search-engine-friendly website structure and optimization designed to improve online visibility.",
                    features = "On-page optimization, Keyword structuring, Fast mobile load speed, Clean meta tags, Search indexing",
                    iconKey = "seo",
                    category = "Marketing",
                    orderIndex = 4,
                    isEnabled = true
                ),
                ServiceEntity(
                    title = "Digital Marketing",
                    description = "Digital marketing solutions that help businesses establish and strengthen their online presence.",
                    features = "Social media growth, Targeted campaigns, Content positioning, Audience engagement strategies",
                    iconKey = "marketing",
                    category = "Marketing",
                    orderIndex = 5,
                    isEnabled = true
                ),
                ServiceEntity(
                    title = "Business Website Development",
                    description = "Tailored corporate websites engineered for credibility, seamless user experience, and conversion.",
                    features = "Interactive product catalogs, Lead capture pipelines, High security, Custom domain setup",
                    iconKey = "business",
                    category = "Web",
                    orderIndex = 6,
                    isEnabled = true
                ),
                ServiceEntity(
                    title = "Landing Page Design",
                    description = "Conversion-focused landing pages crafted to present campaigns and convert visitors into clients.",
                    features = "Attention-grabbing hero, Dynamic call-to-actions, Engaging micro-interactions, Mobile priority",
                    iconKey = "landing",
                    category = "Design",
                    orderIndex = 7,
                    isEnabled = true
                ),
                ServiceEntity(
                    title = "Website Maintenance & Support",
                    description = "Reliable technical maintenance, performance monitoring, updates, and continuous improvements.",
                    features = "Periodic backups, Bug fixes, Speed optimization checks, Content updates",
                    iconKey = "maintenance",
                    category = "Web",
                    orderIndex = 8,
                    isEnabled = true
                )
            )
            for (s in services) dao.insertService(s)

            // Seed Skills
            val skills = listOf(
                SkillEntity(name = "Web Development", category = "Development", proficiencyPercentage = 92, iconKey = "code", orderIndex = 1),
                SkillEntity(name = "UI/UX Design", category = "Design", proficiencyPercentage = 88, iconKey = "palette", orderIndex = 2),
                SkillEntity(name = "Logo Design", category = "Design", proficiencyPercentage = 86, iconKey = "brush", orderIndex = 3),
                SkillEntity(name = "SEO", category = "Marketing", proficiencyPercentage = 85, iconKey = "search", orderIndex = 4),
                SkillEntity(name = "Digital Marketing", category = "Marketing", proficiencyPercentage = 84, iconKey = "trending_up", orderIndex = 5),
                SkillEntity(name = "Data Entry", category = "Data", proficiencyPercentage = 96, iconKey = "table_chart", orderIndex = 6),
                SkillEntity(name = "Responsive Design", category = "Development", proficiencyPercentage = 94, iconKey = "devices", orderIndex = 7),
                SkillEntity(name = "Database Integration", category = "Development", proficiencyPercentage = 88, iconKey = "storage", orderIndex = 8),
                SkillEntity(name = "Business Website Development", category = "Development", proficiencyPercentage = 90, iconKey = "business", orderIndex = 9)
            )
            for (sk in skills) dao.insertSkill(sk)

            // Seed Initial Projects (strictly authentic portfolio representations)
            val projects = listOf(
                ProjectEntity(
                    title = "Talha Mahmood Personal Portfolio",
                    category = "Websites",
                    description = "Full-stack personal portfolio and client management platform showcasing digital services, interactive project gallery, real-time quote estimator, and integrated CRM inbox.",
                    technologies = "Jetpack Compose, Kotlin, Room Database, Material 3, Clean Architecture",
                    projectUrl = "https://talhamahmood.dev",
                    githubUrl = "https://github.com/talhamahmood1055/portfolio",
                    clientName = "Talha Mahmood",
                    dateCompleted = "2026",
                    isFeatured = true,
                    orderIndex = 1
                ),
                ProjectEntity(
                    title = "Trend Nest Website",
                    category = "Websites",
                    description = "Modern lifestyle and trend showcase platform featuring clean product cards, responsive visual hierarchy, and intuitive user navigation.",
                    technologies = "Modern Web, Responsive UI, CSS Grid, Interactive Elements",
                    projectUrl = "https://trendnest.example.com",
                    githubUrl = "https://github.com/talhamahmood1055/trend-nest",
                    clientName = "Brand Portfolio Project",
                    dateCompleted = "2025",
                    isFeatured = true,
                    orderIndex = 2
                ),
                ProjectEntity(
                    title = "Library Management System",
                    category = "Websites",
                    description = "Comprehensive digital library management portal with catalog search, borrowing logs, user account records, and structured database storage.",
                    technologies = "Database Integration, CRUD Operations, Search Filter, Admin Portal",
                    projectUrl = "https://librarysystem.example.com",
                    githubUrl = "https://github.com/talhamahmood1055/library-system",
                    clientName = "Academic / Tech Project",
                    dateCompleted = "2025",
                    isFeatured = true,
                    orderIndex = 3
                ),
                ProjectEntity(
                    title = "Mobile Accessories Business Website",
                    category = "Websites",
                    description = "High-converting online catalog and storefront for mobile accessories with fast category filtering and direct WhatsApp inquiry integration.",
                    technologies = "E-Commerce UI, Category Engine, WhatsApp Ordering, Mobile-First",
                    projectUrl = "https://mobileaccessories.example.com",
                    githubUrl = "https://github.com/talhamahmood1055/accessories-store",
                    clientName = "Client Project",
                    dateCompleted = "2025",
                    isFeatured = true,
                    orderIndex = 4
                ),
                ProjectEntity(
                    title = "Business Proposal Website",
                    category = "Websites",
                    description = "Interactive corporate pitch and proposal website designed for B2B client presentations, clear scope breakdowns, and seamless inquiry capture.",
                    technologies = "Corporate UI, Section Flow, Interactive Proposal, Lead Capture",
                    projectUrl = "https://proposal.example.com",
                    githubUrl = "https://github.com/talhamahmood1055/business-proposal",
                    clientName = "B2B Client Work",
                    dateCompleted = "2026",
                    isFeatured = false,
                    orderIndex = 5
                ),
                ProjectEntity(
                    title = "Logo & Brand Design Projects",
                    category = "Design",
                    description = "Curated collection of brand identity designs, typography systems, vector logos, and visual style guides created for various startup and business concepts.",
                    technologies = "Vector Design, Typography, Color Palette Systems, Figma & Illustrator",
                    projectUrl = "",
                    githubUrl = "https://github.com/talhamahmood1055/brand-designs",
                    clientName = "Design Showcase",
                    dateCompleted = "2026",
                    isFeatured = true,
                    orderIndex = 6
                ),
                ProjectEntity(
                    title = "Custom Client Web Applications",
                    category = "Other",
                    description = "Specialized custom web projects engineered for specific client business requirements, data handling, and automated client workflows.",
                    technologies = "Full-Stack Development, API Integration, Custom Dashboards",
                    projectUrl = "",
                    githubUrl = "https://github.com/talhamahmood1055",
                    clientName = "Client Project Contracts",
                    dateCompleted = "2026",
                    isFeatured = false,
                    orderIndex = 7
                )
            )
            for (p in projects) dao.insertProject(p)

            // Seed Experience Timeline
            val experiences = listOf(
                ExperienceEntity(
                    position = "Digital Creator & Web Developer",
                    organization = "Independent / Freelance Services",
                    period = "2024 — Present",
                    description = "Creating modern websites, responsive frontends, and digital solutions for individuals, startups, and growing businesses.",
                    skillsUsed = "Web Development, UI/UX, SEO, Responsive Layouts",
                    orderIndex = 1
                ),
                ExperienceEntity(
                    position = "Brand & Graphic Designer",
                    organization = "Creative Studio Projects",
                    period = "2023 — 2024",
                    description = "Designed distinct brand identities, vector logos, and visual assets ensuring consistent digital branding across touchpoints.",
                    skillsUsed = "Logo Design, Brand Identity, Visual Styling",
                    orderIndex = 2
                ),
                ExperienceEntity(
                    position = "Data Specialist & Technical Associate",
                    organization = "Tech Services Solutions",
                    period = "2022 — 2023",
                    description = "Conducted accurate data entry, verification, database structuring, and spreadsheet organization for client records.",
                    skillsUsed = "Data Entry, Data Verification, Structured Spreadsheets",
                    orderIndex = 3
                )
            )
            for (e in experiences) dao.insertExperience(e)

            // Seed Testimonial Placeholders (Strictly following: "Do NOT invent fake client reviews... Create editable placeholder reviews in admin dashboard")
            val testimonials = listOf(
                TestimonialEntity(
                    clientName = "Client Review Placeholder 1",
                    clientRole = "Startup Founder",
                    review = "Your real client review will appear here. You can easily add and edit authentic client reviews from the Admin Dashboard.",
                    projectName = "Website Creation",
                    rating = 5,
                    dateAdded = "2026",
                    isPublished = true
                ),
                TestimonialEntity(
                    clientName = "Client Review Placeholder 2",
                    clientRole = "Business Owner",
                    review = "Real customer feedback and ratings will be showcased here once real projects are reviewed. Easily manageable via Admin Panel.",
                    projectName = "Logo & Brand Design",
                    rating = 5,
                    dateAdded = "2026",
                    isPublished = true
                )
            )
            for (t in testimonials) dao.insertTestimonial(t)

            // Seed Site Settings
            dao.saveSetting(SiteSettingEntity("admin_pin", "1055"))
            dao.saveSetting(SiteSettingEntity("seo_title", "Talha Mahmood — Digital Creator & Technology Professional"))
            dao.saveSetting(SiteSettingEntity("seo_desc", "I create modern websites, professional brand identities, and digital solutions that help businesses build trust and grow online."))
            dao.saveSetting(SiteSettingEntity("seo_keywords", "Talha Mahmood, website creator, web developer, logo design, SEO, digital marketing, data entry, Pakistan web designer"))
            dao.saveSetting(SiteSettingEntity("whatsapp_number", "03255691055"))
        }
    }
}
