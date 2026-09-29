package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ProfileEntity
import com.example.ui.components.DirectContactActionsBar
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseError
import com.example.viewmodel.PortfolioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    viewModel: PortfolioViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profile.collectAsState()
    val formState by viewModel.contactFormState.collectAsState()
    val currentProfile = profile ?: ProfileEntity()

    val serviceOptions = listOf(
        "Website Creation",
        "Logo & Brand Design",
        "Data Entry",
        "SEO",
        "Digital Marketing",
        "Business Website Development",
        "Landing Page Design",
        "Custom Website Project",
        "Other"
    )

    val budgetOptions = listOf(
        "$100 - $300",
        "$300 - $600",
        "$600 - $1,200",
        "$1,200+",
        "Custom / Flexible"
    )

    var serviceDropdownExpanded by remember { mutableStateOf(false) }
    var budgetDropdownExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            SectionHeader(
                tag = "Contact & Hire",
                title = "Let's Start Your Project",
                subtitle = "Fill out the project request form below or reach out directly through WhatsApp, phone, or email."
            )
        }

        // Direct Channels Card
        item {
            DirectContactActionsBar(
                phone = currentProfile.phone,
                email = currentProfile.email,
                whatsapp = currentProfile.whatsapp
            )
        }

        // Form Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                        RoundedCornerShape(20.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Project Request Form",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Tell me about your goals, timelines, and requirements.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Error Message
                    AnimatedVisibility(visible = formState.errorMessage != null) {
                        Surface(
                            color = RoseError.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = formState.errorMessage ?: "",
                                color = RoseError,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    // Success Message
                    AnimatedVisibility(visible = formState.successMessage != null) {
                        Surface(
                            color = EmeraldSuccess.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = formState.successMessage ?: "",
                                    color = EmeraldSuccess,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    // Name
                    OutlinedTextField(
                        value = formState.name,
                        onValueChange = { viewModel.updateContactFormField(name = it) },
                        label = { Text("Your Full Name *") },
                        placeholder = { Text("e.g. John Doe") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Email
                    OutlinedTextField(
                        value = formState.email,
                        onValueChange = { viewModel.updateContactFormField(email = it) },
                        label = { Text("Email Address *") },
                        placeholder = { Text("name@example.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Phone / WhatsApp
                    OutlinedTextField(
                        value = formState.phone,
                        onValueChange = { viewModel.updateContactFormField(phone = it) },
                        label = { Text("Phone / WhatsApp Number") },
                        placeholder = { Text("e.g. 03255691055") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Company
                    OutlinedTextField(
                        value = formState.company,
                        onValueChange = { viewModel.updateContactFormField(company = it) },
                        label = { Text("Company / Business Name") },
                        placeholder = { Text("e.g. Startup, Agency, Personal") },
                        leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Service Dropdown
                    ExposedDropdownMenuBox(
                        expanded = serviceDropdownExpanded,
                        onExpandedChange = { serviceDropdownExpanded = !serviceDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = formState.serviceRequired,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Service Required") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = serviceDropdownExpanded,
                            onDismissRequest = { serviceDropdownExpanded = false }
                        ) {
                            serviceOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        viewModel.updateContactFormField(service = opt)
                                        serviceDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Budget Dropdown
                    ExposedDropdownMenuBox(
                        expanded = budgetDropdownExpanded,
                        onExpandedChange = { budgetDropdownExpanded = !budgetDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = formState.budget,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Estimated Budget") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = budgetDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = budgetDropdownExpanded,
                            onDismissRequest = { budgetDropdownExpanded = false }
                        ) {
                            budgetOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        viewModel.updateContactFormField(budget = opt)
                                        budgetDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Message
                    OutlinedTextField(
                        value = formState.message,
                        onValueChange = { viewModel.updateContactFormField(message = it) },
                        label = { Text("Project Details / Message *") },
                        placeholder = { Text("Describe what you need built, key features, target deadline...") },
                        minLines = 4,
                        maxLines = 8,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Submit Button
                    Button(
                        onClick = { viewModel.submitContactForm() },
                        enabled = !formState.isSubmitting,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        if (formState.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sending Request...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Send Project Request",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
