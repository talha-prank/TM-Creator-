package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.models.ContactMessageEntity
import com.example.data.models.ExperienceEntity
import com.example.data.models.ProfileEntity
import com.example.data.models.ProjectEntity
import com.example.data.models.ServiceEntity
import com.example.data.models.SkillEntity
import com.example.data.models.TestimonialEntity
import com.example.data.repository.PortfolioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    SERVICES,
    PROJECTS,
    CONTACT,
    ADMIN
}

enum class AdminTab {
    CRM_LEADS,
    PROFILE,
    PROJECTS,
    SERVICES,
    SKILLS,
    TESTIMONIALS,
    SETTINGS
}

data class ContactFormState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val company: String = "",
    val serviceRequired: String = "Website Creation",
    val budget: String = "$150 - $400",
    val message: String = "",
    val isSubmitting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PortfolioRepository

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = PortfolioRepository(database.portfolioDao())

        // Check if DB was initialized, if empty seed immediately
        viewModelScope.launch {
            val existing = repository.profile.firstOrNull()
            if (existing == null) {
                AppDatabase.populateInitialData(database.portfolioDao())
            }
        }
    }

    // --- State Streams ---
    val profile: StateFlow<ProfileEntity?> = repository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileEntity())

    val services: StateFlow<List<ServiceEntity>> = repository.allServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val enabledServices: StateFlow<List<ServiceEntity>> = repository.enabledServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val skills: StateFlow<List<SkillEntity>> = repository.allSkills
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredProjects: StateFlow<List<ProjectEntity>> = repository.featuredProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val experiences: StateFlow<List<ExperienceEntity>> = repository.allExperiences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val testimonials: StateFlow<List<TestimonialEntity>> = repository.allTestimonials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val publishedTestimonials: StateFlow<List<TestimonialEntity>> = repository.publishedTestimonials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val contactMessages: StateFlow<List<ContactMessageEntity>> = repository.allContactMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- App Navigation & UI State ---
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedProjectCategory = MutableStateFlow("All")
    val selectedProjectCategory: StateFlow<String> = _selectedProjectCategory.asStateFlow()

    private val _projectSearchQuery = MutableStateFlow("")
    val projectSearchQuery: StateFlow<String> = _projectSearchQuery.asStateFlow()

    private val _selectedProjectForDetail = MutableStateFlow<ProjectEntity?>(null)
    val selectedProjectForDetail: StateFlow<ProjectEntity?> = _selectedProjectForDetail.asStateFlow()

    private val _contactFormState = MutableStateFlow(ContactFormState())
    val contactFormState: StateFlow<ContactFormState> = _contactFormState.asStateFlow()

    // --- Admin State ---
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _adminPinInput = MutableStateFlow("")
    val adminPinInput: StateFlow<String> = _adminPinInput.asStateFlow()

    private val _adminPinError = MutableStateFlow<String?>(null)
    val adminPinError: StateFlow<String?> = _adminPinError.asStateFlow()

    private val _selectedAdminTab = MutableStateFlow(AdminTab.CRM_LEADS)
    val selectedAdminTab: StateFlow<AdminTab> = _selectedAdminTab.asStateFlow()

    private val _crmStatusFilter = MutableStateFlow("All")
    val crmStatusFilter: StateFlow<String> = _crmStatusFilter.asStateFlow()

    private val _crmSearchQuery = MutableStateFlow("")
    val crmSearchQuery: StateFlow<String> = _crmSearchQuery.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // --- Navigation Actions ---
    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectProjectCategory(category: String) {
        _selectedProjectCategory.value = category
    }

    fun setProjectSearchQuery(query: String) {
        _projectSearchQuery.value = query
    }

    fun openProjectDetail(project: ProjectEntity) {
        _selectedProjectForDetail.value = project
    }

    fun closeProjectDetail() {
        _selectedProjectForDetail.value = null
    }

    fun dismissToast() {
        _toastMessage.value = null
    }

    // --- Contact Form Actions ---
    fun updateContactFormField(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        company: String? = null,
        service: String? = null,
        budget: String? = null,
        message: String? = null
    ) {
        _contactFormState.value = _contactFormState.value.copy(
            name = name ?: _contactFormState.value.name,
            email = email ?: _contactFormState.value.email,
            phone = phone ?: _contactFormState.value.phone,
            company = company ?: _contactFormState.value.company,
            serviceRequired = service ?: _contactFormState.value.serviceRequired,
            budget = budget ?: _contactFormState.value.budget,
            message = message ?: _contactFormState.value.message,
            successMessage = null,
            errorMessage = null
        )
    }

    fun prefillContactWithService(serviceName: String, budget: String = "$150 - $400") {
        _contactFormState.value = _contactFormState.value.copy(
            serviceRequired = serviceName,
            budget = budget
        )
        _currentScreen.value = Screen.CONTACT
    }

    fun submitContactForm() {
        val state = _contactFormState.value
        if (state.name.isBlank()) {
            _contactFormState.value = state.copy(errorMessage = "Please enter your name.")
            return
        }
        if (state.email.isBlank() || !state.email.contains("@")) {
            _contactFormState.value = state.copy(errorMessage = "Please enter a valid email address.")
            return
        }
        if (state.message.isBlank() || state.message.length < 5) {
            _contactFormState.value = state.copy(errorMessage = "Please include a brief message about your project.")
            return
        }

        viewModelScope.launch {
            _contactFormState.value = state.copy(isSubmitting = true)
            try {
                val entity = ContactMessageEntity(
                    name = state.name.trim(),
                    email = state.email.trim(),
                    phone = state.phone.trim(),
                    company = state.company.trim(),
                    serviceRequired = state.serviceRequired,
                    budget = state.budget,
                    message = state.message.trim(),
                    timestamp = System.currentTimeMillis(),
                    status = "New"
                )
                repository.submitContactMessage(entity)
                _contactFormState.value = ContactFormState(
                    successMessage = "Thank you! Your project request has been sent to Talha Mahmood. We will reach out shortly."
                )
                _toastMessage.value = "Project request sent successfully!"
            } catch (e: Exception) {
                _contactFormState.value = state.copy(
                    isSubmitting = false,
                    errorMessage = "Failed to send request: ${e.localizedMessage}"
                )
            }
        }
    }

    // --- Admin Authentication & Controls ---
    fun updateAdminPinInput(pin: String) {
        _adminPinInput.value = pin
        _adminPinError.value = null
    }

    fun authenticateAdmin() {
        viewModelScope.launch {
            val configuredPin = repository.getSettingValue("admin_pin") ?: "1055"
            if (_adminPinInput.value == configuredPin) {
                _isAdminAuthenticated.value = true
                _adminPinError.value = null
                _adminPinInput.value = ""
                _toastMessage.value = "Welcome to Talha Mahmood Admin Dashboard"
            } else {
                _adminPinError.value = "Incorrect PIN. (Default: 1055)"
            }
        }
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        _currentScreen.value = Screen.HOME
        _toastMessage.value = "Admin logged out"
    }

    fun selectAdminTab(tab: AdminTab) {
        _selectedAdminTab.value = tab
    }

    fun setCrmStatusFilter(status: String) {
        _crmStatusFilter.value = status
    }

    fun setCrmSearchQuery(query: String) {
        _crmSearchQuery.value = query
    }

    fun updateLeadStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateMessageStatus(id, status)
            _toastMessage.value = "Lead status updated to $status"
        }
    }

    fun deleteLead(message: ContactMessageEntity) {
        viewModelScope.launch {
            repository.deleteContactMessage(message)
            _toastMessage.value = "Lead deleted"
        }
    }

    // --- Admin Profile Save ---
    fun saveProfile(profile: ProfileEntity) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            _toastMessage.value = "Profile updated successfully"
        }
    }

    // --- Admin Projects CRUD ---
    fun saveProject(project: ProjectEntity) {
        viewModelScope.launch {
            if (project.id == 0) {
                repository.addProject(project)
                _toastMessage.value = "Project added"
            } else {
                repository.updateProject(project)
                _toastMessage.value = "Project updated"
            }
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.deleteProject(project)
            _toastMessage.value = "Project deleted"
        }
    }

    // --- Admin Services CRUD ---
    fun saveService(service: ServiceEntity) {
        viewModelScope.launch {
            if (service.id == 0) {
                repository.addService(service)
                _toastMessage.value = "Service added"
            } else {
                repository.updateService(service)
                _toastMessage.value = "Service updated"
            }
        }
    }

    fun deleteService(service: ServiceEntity) {
        viewModelScope.launch {
            repository.deleteService(service)
            _toastMessage.value = "Service removed"
        }
    }

    // --- Admin Skills CRUD ---
    fun saveSkill(skill: SkillEntity) {
        viewModelScope.launch {
            if (skill.id == 0) {
                repository.addSkill(skill)
                _toastMessage.value = "Skill saved"
            } else {
                repository.updateSkill(skill)
                _toastMessage.value = "Skill updated"
            }
        }
    }

    fun deleteSkill(skill: SkillEntity) {
        viewModelScope.launch {
            repository.deleteSkill(skill)
            _toastMessage.value = "Skill removed"
        }
    }

    // --- Admin Testimonials CRUD (strictly authentic/editable) ---
    fun saveTestimonial(testimonial: TestimonialEntity) {
        viewModelScope.launch {
            if (testimonial.id == 0) {
                repository.addTestimonial(testimonial)
                _toastMessage.value = "Testimonial review added"
            } else {
                repository.updateTestimonial(testimonial)
                _toastMessage.value = "Testimonial updated"
            }
        }
    }

    fun deleteTestimonial(testimonial: TestimonialEntity) {
        viewModelScope.launch {
            repository.deleteTestimonial(testimonial)
            _toastMessage.value = "Testimonial removed"
        }
    }

    // --- Admin Settings ---
    fun updateAdminPin(newPin: String) {
        if (newPin.length < 4) {
            _toastMessage.value = "PIN must be at least 4 digits"
            return
        }
        viewModelScope.launch {
            repository.saveSetting("admin_pin", newPin)
            _toastMessage.value = "Admin PIN updated successfully"
        }
    }
}
