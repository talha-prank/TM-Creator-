package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.*
import com.example.ui.components.openEmail
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.*
import com.example.viewmodel.AdminTab
import com.example.viewmodel.PortfolioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val isAuthenticated by viewModel.isAdminAuthenticated.collectAsState()
    val pinInput by viewModel.adminPinInput.collectAsState()
    val pinError by viewModel.adminPinError.collectAsState()

    if (!isAuthenticated) {
        AdminLoginGate(
            pinInput = pinInput,
            pinError = pinError,
            onPinChange = { viewModel.updateAdminPinInput(it) },
            onAuthenticate = { viewModel.authenticateAdmin() },
            modifier = modifier
        )
    } else {
        AdminDashboardContent(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
fun AdminLoginGate(
    pinInput: String,
    pinError: String?,
    onPinChange: (String) -> Unit,
    onAuthenticate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = CyanPrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Talha Mahmood Admin",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                )

                Text(
                    text = "Enter your security PIN to access the CRM Leads and Content Management Dashboard.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(visible = pinError != null) {
                    Surface(
                        color = RoseError.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = pinError ?: "",
                            color = RoseError,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = pinInput,
                    onValueChange = onPinChange,
                    label = { Text("Admin PIN") },
                    placeholder = { Text("Default: 1055") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onAuthenticate,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        "Unlock Dashboard",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Tip: Default PIN is 1055 (changeable in settings)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Composable
fun AdminDashboardContent(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTab by viewModel.selectedAdminTab.collectAsState()
    val contactMessages by viewModel.contactMessages.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val services by viewModel.services.collectAsState()
    val skills by viewModel.skills.collectAsState()
    val testimonials by viewModel.testimonials.collectAsState()
    val profile by viewModel.profile.collectAsState()

    val currentProfile = profile ?: ProfileEntity()
    val newLeadsCount = contactMessages.count { it.status == "New" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dashboard Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Admin Management Portal",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Logged in as ${currentProfile.name}",
                                style = MaterialTheme.typography.labelSmall.copy(color = CyanPrimary)
                            )
                        }

                        IconButton(onClick = { viewModel.logoutAdmin() }) {
                            Icon(Icons.Default.Logout, contentDescription = "Logout", tint = RoseError)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // KPI Metrics Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KpiMetricCard(
                            label = "Leads Inbox",
                            value = "${contactMessages.size}",
                            highlight = if (newLeadsCount > 0) "$newLeadsCount New" else null,
                            modifier = Modifier.weight(1f)
                        )
                        KpiMetricCard(
                            label = "Projects",
                            value = "${projects.size}",
                            highlight = "Active",
                            modifier = Modifier.weight(1f)
                        )
                        KpiMetricCard(
                            label = "Services",
                            value = "${services.size}",
                            highlight = null,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Tab Navigation Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val tabs = listOf(
                    Pair(AdminTab.CRM_LEADS, "Leads CRM (${contactMessages.size})"),
                    Pair(AdminTab.PROFILE, "Profile & Bio"),
                    Pair(AdminTab.PROJECTS, "Projects (${projects.size})"),
                    Pair(AdminTab.SERVICES, "Services (${services.size})"),
                    Pair(AdminTab.SKILLS, "Skills (${skills.size})"),
                    Pair(AdminTab.TESTIMONIALS, "Testimonials (${testimonials.size})"),
                    Pair(AdminTab.SETTINGS, "Settings & PIN")
                )

                items(tabs) { (tab, label) ->
                    val isSelected = selectedTab == tab
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectAdminTab(tab) },
                        label = { Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            AdminTab.CRM_LEADS -> {
                item {
                    CrmLeadsSection(
                        messages = contactMessages,
                        onUpdateStatus = { id, st -> viewModel.updateLeadStatus(id, st) },
                        onDelete = { msg -> viewModel.deleteLead(msg) }
                    )
                }
            }
            AdminTab.PROFILE -> {
                item {
                    ProfileEditorSection(
                        profile = currentProfile,
                        onSave = { updated -> viewModel.saveProfile(updated) }
                    )
                }
            }
            AdminTab.PROJECTS -> {
                item {
                    ProjectsManagerSection(
                        projects = projects,
                        onSaveProject = { p -> viewModel.saveProject(p) },
                        onDeleteProject = { p -> viewModel.deleteProject(p) }
                    )
                }
            }
            AdminTab.SERVICES -> {
                item {
                    ServicesManagerSection(
                        services = services,
                        onSaveService = { s -> viewModel.saveService(s) },
                        onDeleteService = { s -> viewModel.deleteService(s) }
                    )
                }
            }
            AdminTab.SKILLS -> {
                item {
                    SkillsManagerSection(
                        skills = skills,
                        onSaveSkill = { sk -> viewModel.saveSkill(sk) },
                        onDeleteSkill = { sk -> viewModel.deleteSkill(sk) }
                    )
                }
            }
            AdminTab.TESTIMONIALS -> {
                item {
                    TestimonialsManagerSection(
                        testimonials = testimonials,
                        onSaveTestimonial = { t -> viewModel.saveTestimonial(t) },
                        onDeleteTestimonial = { t -> viewModel.deleteTestimonial(t) }
                    )
                }
            }
            AdminTab.SETTINGS -> {
                item {
                    SettingsManagerSection(
                        onUpdatePin = { newPin -> viewModel.updateAdminPin(newPin) }
                    )
                }
            }
        }
    }
}

@Composable
fun KpiMetricCard(
    label: String,
    value: String,
    highlight: String?,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                )
                if (highlight != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = highlight,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}

// --- 1. CRM LEADS SECTION ---
@Composable
fun CrmLeadsSection(
    messages: List<ContactMessageEntity>,
    onUpdateStatus: (Int, String) -> Unit,
    onDelete: (ContactMessageEntity) -> Unit
) {
    val context = LocalContext.current
    var statusFilter by remember { mutableStateOf("All") }
    val statuses = listOf("All", "New", "Contacted", "In Progress", "Completed", "Archived")

    val filtered = remember(messages, statusFilter) {
        if (statusFilter == "All") messages else messages.filter { it.status == statusFilter }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            statuses.take(3).forEach { st ->
                FilterChip(
                    selected = statusFilter == st,
                    onClick = { statusFilter = st },
                    label = { Text(st, fontSize = 11.sp) }
                )
            }
            statuses.drop(3).forEach { st ->
                FilterChip(
                    selected = statusFilter == st,
                    onClick = { statusFilter = st },
                    label = { Text(st, fontSize = 11.sp) }
                )
            }
        }

        if (filtered.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No messages in this status category.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            filtered.forEach { msg ->
                val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                val dateStr = dateFormat.format(Date(msg.timestamp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = msg.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            StatusPill(status = msg.status)
                        }

                        if (msg.company.isNotBlank()) {
                            Text(
                                text = "Company: ${msg.company}",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
                            )
                        }

                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Service: ${msg.serviceRequired}", style = MaterialTheme.typography.labelSmall)
                                Text("Budget: ${msg.budget}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = msg.message,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Actions: WhatsApp reply, email reply, status selector, delete
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (msg.phone.isNotBlank()) {
                                    IconButton(
                                        onClick = { openWhatsApp(context, msg.phone) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = EmeraldSuccess)
                                    }
                                }
                                IconButton(
                                    onClick = { openEmail(context, msg.email) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Email, contentDescription = "Email", tint = CyanPrimary)
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                var statusMenuOpen by remember { mutableStateOf(false) }
                                Box {
                                    OutlinedButton(
                                        onClick = { statusMenuOpen = true },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("Status: ${msg.status}", style = MaterialTheme.typography.labelSmall)
                                    }
                                    DropdownMenu(
                                        expanded = statusMenuOpen,
                                        onDismissRequest = { statusMenuOpen = false }
                                    ) {
                                        statuses.filter { it != "All" }.forEach { s ->
                                            DropdownMenuItem(
                                                text = { Text(s) },
                                                onClick = {
                                                    onUpdateStatus(msg.id, s)
                                                    statusMenuOpen = false
                                                }
                                            )
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = { onDelete(msg) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseError)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusPill(status: String) {
    val (bg, fg) = when (status) {
        "New" -> Pair(CyanPrimary.copy(alpha = 0.15f), CyanPrimary)
        "Contacted" -> Pair(AmberAccent.copy(alpha = 0.15f), AmberAccent)
        "In Progress" -> Pair(PurpleAccent.copy(alpha = 0.15f), PurpleAccent)
        "Completed" -> Pair(EmeraldSuccess.copy(alpha = 0.15f), EmeraldSuccess)
        else -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Surface(color = bg, shape = RoundedCornerShape(100.dp)) {
        Text(
            text = status.uppercase(),
            color = fg,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

// --- 2. PROFILE EDITOR SECTION ---
@Composable
fun ProfileEditorSection(
    profile: ProfileEntity,
    onSave: (ProfileEntity) -> Unit
) {
    var name by remember(profile) { mutableStateOf(profile.name) }
    var brandTitle by remember(profile) { mutableStateOf(profile.brandTitle) }
    var profileImageUrl by remember(profile) { mutableStateOf(profile.profileImageUrl) }
    var heroHeadline by remember(profile) { mutableStateOf(profile.heroHeadline) }
    var heroSubheadline by remember(profile) { mutableStateOf(profile.heroSubheadline) }
    var longBio by remember(profile) { mutableStateOf(profile.longBio) }
    var phone by remember(profile) { mutableStateOf(profile.phone) }
    var email by remember(profile) { mutableStateOf(profile.email) }
    var whatsapp by remember(profile) { mutableStateOf(profile.whatsapp) }
    var location by remember(profile) { mutableStateOf(profile.location) }
    var availabilityStatus by remember(profile) { mutableStateOf(profile.availabilityStatus) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Edit Profile & Brand Identity", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = brandTitle, onValueChange = { brandTitle = it }, label = { Text("Brand Subtitle") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = profileImageUrl, onValueChange = { profileImageUrl = it }, label = { Text("Profile Picture URL") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = heroHeadline, onValueChange = { heroHeadline = it }, label = { Text("Hero Headline") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = heroSubheadline, onValueChange = { heroSubheadline = it }, label = { Text("Hero Subheadline") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = longBio, onValueChange = { longBio = it }, label = { Text("About Bio Text") }, minLines = 4, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = whatsapp, onValueChange = { whatsapp = it }, label = { Text("WhatsApp Number") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = availabilityStatus, onValueChange = { availabilityStatus = it }, label = { Text("Availability Status") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onSave(
                        profile.copy(
                            name = name,
                            brandTitle = brandTitle,
                            profileImageUrl = profileImageUrl,
                            heroHeadline = heroHeadline,
                            heroSubheadline = heroSubheadline,
                            longBio = longBio,
                            email = email,
                            phone = phone,
                            whatsapp = whatsapp,
                            location = location,
                            availabilityStatus = availabilityStatus
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
            ) {
                Text("Save Profile Changes", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

// --- 3. PROJECTS MANAGER SECTION ---
@Composable
fun ProjectsManagerSection(
    projects: List<ProjectEntity>,
    onSaveProject: (ProjectEntity) -> Unit,
    onDeleteProject: (ProjectEntity) -> Unit
) {
    var editingProject by remember { mutableStateOf<ProjectEntity?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Button(
            onClick = {
                editingProject = ProjectEntity(
                    title = "",
                    category = "Websites",
                    description = "",
                    technologies = "",
                    projectUrl = "",
                    githubUrl = "",
                    clientName = ""
                )
                isAddingNew = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add New Portfolio Project", fontWeight = FontWeight.Bold)
        }

        projects.forEach { project ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(project.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("${project.category} • ${project.dateCompleted}", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary))
                        Text(project.technologies, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Row {
                        IconButton(onClick = {
                            editingProject = project
                            isAddingNew = false
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CyanPrimary)
                        }
                        IconButton(onClick = { onDeleteProject(project) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseError)
                        }
                    }
                }
            }
        }
    }

    editingProject?.let { p ->
        ProjectEditorDialog(
            project = p,
            isNew = isAddingNew,
            onDismiss = { editingProject = null },
            onSave = { updated ->
                onSaveProject(updated)
                editingProject = null
            }
        )
    }
}

@Composable
fun ProjectEditorDialog(
    project: ProjectEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (ProjectEntity) -> Unit
) {
    var title by remember { mutableStateOf(project.title) }
    var category by remember { mutableStateOf(project.category) }
    var description by remember { mutableStateOf(project.description) }
    var technologies by remember { mutableStateOf(project.technologies) }
    var projectUrl by remember { mutableStateOf(project.projectUrl) }
    var githubUrl by remember { mutableStateOf(project.githubUrl) }
    var clientName by remember { mutableStateOf(project.clientName) }
    var isFeatured by remember { mutableStateOf(project.isFeatured) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(if (isNew) "Add Project" else "Edit Project", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category (Websites, Design, SEO, Other)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, minLines = 3, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = technologies, onValueChange = { technologies = it }, label = { Text("Tech Stack (comma separated)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = projectUrl, onValueChange = { projectUrl = it }, label = { Text("Project Live URL") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = githubUrl, onValueChange = { githubUrl = it }, label = { Text("GitHub URL") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(value = clientName, onValueChange = { clientName = it }, label = { Text("Client / Project Scope") }, modifier = Modifier.fillMaxWidth())

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                    Text("Featured on Homepage", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onSave(
                            project.copy(
                                title = title,
                                category = category,
                                description = description,
                                technologies = technologies,
                                projectUrl = projectUrl,
                                githubUrl = githubUrl,
                                clientName = clientName,
                                isFeatured = isFeatured
                            )
                        )
                    }) { Text("Save") }
                }
            }
        }
    }
}

// --- 4. SERVICES MANAGER SECTION ---
@Composable
fun ServicesManagerSection(
    services: List<ServiceEntity>,
    onSaveService: (ServiceEntity) -> Unit,
    onDeleteService: (ServiceEntity) -> Unit
) {
    var editingService by remember { mutableStateOf<ServiceEntity?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {
                editingService = ServiceEntity(
                    title = "",
                    description = "",
                    features = "",
                    iconKey = "web"
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add New Service", fontWeight = FontWeight.Bold)
        }

        services.forEach { service ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(service.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(service.description, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Row {
                        IconButton(onClick = { editingService = service }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CyanPrimary)
                        }
                        IconButton(onClick = { onDeleteService(service) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseError)
                        }
                    }
                }
            }
        }
    }

    editingService?.let { s ->
        var title by remember { mutableStateOf(s.title) }
        var description by remember { mutableStateOf(s.description) }
        var features by remember { mutableStateOf(s.features) }

        Dialog(onDismissRequest = { editingService = null }) {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Service Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = features, onValueChange = { features = it }, label = { Text("Features (comma separated)") }, minLines = 2, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { editingService = null }) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            onSaveService(s.copy(title = title, description = description, features = features))
                            editingService = null
                        }) { Text("Save") }
                    }
                }
            }
        }
    }
}

// --- 5. SKILLS MANAGER SECTION ---
@Composable
fun SkillsManagerSection(
    skills: List<SkillEntity>,
    onSaveSkill: (SkillEntity) -> Unit,
    onDeleteSkill: (SkillEntity) -> Unit
) {
    var editingSkill by remember { mutableStateOf<SkillEntity?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {
                editingSkill = SkillEntity(name = "", category = "Development", proficiencyPercentage = 85)
            },
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add Skill", fontWeight = FontWeight.Bold)
        }

        skills.forEach { sk ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(sk.name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text("${sk.category} • ${sk.proficiencyPercentage}%", style = MaterialTheme.typography.labelSmall.copy(color = CyanPrimary))
                    }

                    Row {
                        IconButton(onClick = { editingSkill = sk }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CyanPrimary)
                        }
                        IconButton(onClick = { onDeleteSkill(sk) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseError)
                        }
                    }
                }
            }
        }
    }

    editingSkill?.let { sk ->
        var name by remember { mutableStateOf(sk.name) }
        var category by remember { mutableStateOf(sk.category) }
        var percentage by remember { mutableStateOf(sk.proficiencyPercentage.toString()) }

        Dialog(onDismissRequest = { editingSkill = null }) {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Skill Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Skill Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = percentage, onValueChange = { percentage = it }, label = { Text("Proficiency % (1-100)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { editingSkill = null }) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            val pct = percentage.toIntOrNull()?.coerceIn(1, 100) ?: 85
                            onSaveSkill(sk.copy(name = name, category = category, proficiencyPercentage = pct))
                            editingSkill = null
                        }) { Text("Save") }
                    }
                }
            }
        }
    }
}

// --- 6. TESTIMONIALS MANAGER SECTION ---
@Composable
fun TestimonialsManagerSection(
    testimonials: List<TestimonialEntity>,
    onSaveTestimonial: (TestimonialEntity) -> Unit,
    onDeleteTestimonial: (TestimonialEntity) -> Unit
) {
    var editingTestimonial by remember { mutableStateOf<TestimonialEntity?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Honest guidance card
        Card(
            colors = CardDefaults.cardColors(containerColor = CyanPrimary.copy(alpha = 0.08f)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Add genuine customer reviews as you complete projects. Placeholders can be edited or replaced here.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(12.dp)
            )
        }

        Button(
            onClick = {
                editingTestimonial = TestimonialEntity(
                    clientName = "",
                    clientRole = "Client",
                    review = "",
                    projectName = "Website Project",
                    rating = 5,
                    isPublished = true
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add Real Client Review", fontWeight = FontWeight.Bold)
        }

        testimonials.forEach { t ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(t.clientName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text("${t.clientRole} • ${t.projectName} (${t.rating}★)", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary))
                        Text("\"${t.review}\"", style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }

                    Row {
                        IconButton(onClick = { editingTestimonial = t }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CyanPrimary)
                        }
                        IconButton(onClick = { onDeleteTestimonial(t) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseError)
                        }
                    }
                }
            }
        }
    }

    editingTestimonial?.let { t ->
        var clientName by remember { mutableStateOf(t.clientName) }
        var clientRole by remember { mutableStateOf(t.clientRole) }
        var review by remember { mutableStateOf(t.review) }
        var projectName by remember { mutableStateOf(t.projectName) }
        var rating by remember { mutableStateOf(t.rating.toString()) }
        var isPublished by remember { mutableStateOf(t.isPublished) }

        Dialog(onDismissRequest = { editingTestimonial = null }) {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Client Review", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = clientName, onValueChange = { clientName = it }, label = { Text("Client Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = clientRole, onValueChange = { clientRole = it }, label = { Text("Role / Company") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = projectName, onValueChange = { projectName = it }, label = { Text("Project Completed") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = rating, onValueChange = { rating = it }, label = { Text("Rating (1-5)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = review, onValueChange = { review = it }, label = { Text("Review Text") }, minLines = 3, modifier = Modifier.fillMaxWidth())

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isPublished, onCheckedChange = { isPublished = it })
                        Text("Published on Site", style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { editingTestimonial = null }) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            val r = rating.toIntOrNull()?.coerceIn(1, 5) ?: 5
                            onSaveTestimonial(t.copy(clientName = clientName, clientRole = clientRole, review = review, projectName = projectName, rating = r, isPublished = isPublished))
                            editingTestimonial = null
                        }) { Text("Save") }
                    }
                }
            }
        }
    }
}

// --- 7. SETTINGS MANAGER SECTION ---
@Composable
fun SettingsManagerSection(
    onUpdatePin: (String) -> Unit
) {
    var newPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Admin Security & PIN", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text("Update your admin dashboard access code.", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))

            Spacer(modifier = Modifier.height(12.dp))

            if (pinError != null) {
                Text(pinError ?: "", color = RoseError, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(6.dp))
            }

            OutlinedTextField(
                value = newPin,
                onValueChange = { newPin = it; pinError = null },
                label = { Text("New PIN (min 4 digits)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPin,
                onValueChange = { confirmPin = it; pinError = null },
                label = { Text("Confirm New PIN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    if (newPin.length < 4) {
                        pinError = "PIN must be at least 4 digits"
                    } else if (newPin != confirmPin) {
                        pinError = "PINs do not match"
                    } else {
                        onUpdatePin(newPin)
                        newPin = ""
                        confirmPin = ""
                        pinError = null
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
            ) {
                Text("Change Admin PIN", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
