# Talha Mahmood — Digital Creator & Technology Professional
### Official Android Portfolio & Client Management Platform

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Room Database](https://img.shields.io/badge/Room-SQLite%20ORM-4285F4)](https://developer.android.com/training/data-storage/room)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📱 About The App

This application is the official mobile platform for **Talha Mahmood — Digital Creator & Technology Professional**. It is designed to showcase digital services, demonstrate authentic case studies and portfolio projects, provide an interactive project cost & scope estimator, collect and store client inquiries, and empower real-time administration through an on-device CRM & Content Management Dashboard.

---

## ✨ Key Features

### 1. 🌟 Hero & Personal Brand Showcase
- **Dynamic Portrait**: Loads high-resolution portrait art with fallback caching.
- **Availability Indicator**: Live *"Available for New Projects"* badge.
- **Core Trust Points**: Professional Service, Responsive Communication, Custom Solutions, and Client-Focused Work.
- **Instant CTAs**: *"Start a Project"* and *"View My Work"*.

### 2. 💼 Services & Interactive Cost Estimator
- **Core Services**:
  - Website Creation (Business websites, portfolios, landing pages, admin dashboards)
  - Logo & Brand Design (Visual identities, typography, vector assets)
  - Data Entry (Clean structuring, validation, verification)
  - SEO (Search Engine Optimization, on-page structures, speed)
  - Digital Marketing (Social media strategy, campaign growth)
- **Interactive Project Estimator**: Allows prospective clients to customize scope, toggle add-ons, calculate budget ranges, and auto-populate inquiry requests.

### 3. 📂 Projects & Case Studies Gallery
- Real portfolio projects including *Talha Mahmood Portfolio*, *Trend Nest*, *Library Management System*, *Mobile Accessories Store*, *Business Proposal Web*, and *Logo/Brand Projects*.
- Real-time category filtering (*All, Websites, Design, SEO, Marketing, Other*) and keyword search.
- Interactive Project Detail Sheet with tech stack tags, live URLs, and GitHub links.

### 4. 💬 Honest Testimonials
- Fully compliant with authentic review standards (zero invented fake claims).
- Editable review placeholders that can be updated with real customer reviews via the Admin Dashboard.

### 5. 📬 Direct Communication & Inquiries
- **Direct Channels**: One-tap WhatsApp chat (`03255691055`), phone call, and email (`talhamahmood1055@gmail.com`).
- **Comprehensive Project Request Form**: Name, email, phone, company, service dropdown, budget range, and message.
- Submissions are persistently saved directly into the local Room database CRM.

### 6. 🛡️ Secure Admin Dashboard & CRM
- Protected by PIN authentication (Default: `1055`, changeable in settings).
- **CRM Leads Inbox**: View client inquiries, update lead status (*New, Contacted, In Progress, Completed, Archived*), and launch direct WhatsApp or email responses.
- **Content Management**: Dynamically edit Profile, Bio, Hero, Services, Skills, Projects, and Testimonials in real-time.

---

## 🛠️ Architecture & Tech Stack

- **UI Framework**: Modern Jetpack Compose with Material Design 3 (M3).
- **Design Palette**: Deep Midnight Navy (`#070B19`), Electric Cyan (`#00D2FF`), Amber Accent (`#FBBF24`), Emerald (`#10B981`).
- **State Management**: MVVM Architecture (`ViewModel`, `MutableStateFlow`, `collectAsStateWithLifecycle`).
- **Local Persistence**: Android Room Database with KSP (Kotlin Symbol Processing) and Coroutine Flow.
- **Image Pipeline**: Coil (Coroutine Image Loader) with offline drawable fallbacks.
- **System Integration**: Edge-to-Edge display with `WindowInsets.safeDrawing`, Material navigation bar, and backstack handling.

---

## 🚀 How to Run & Build

### Option 1: Automatic Build via GitHub Actions (No local install needed)
1. Go to the **Actions** tab in this GitHub repository.
2. Click on the latest workflow run under **Build Android APK & Bundle**.
3. Under **Artifacts**, download `Talha-Mahmood-Portfolio-APK`.
4. Unzip and transfer the `.apk` file to your Android phone to install and test!

### Option 2: Open in Android Studio
1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   ```
2. Open Android Studio and select **Open**, then choose the cloned folder.
3. Allow Gradle to sync.
4. Connect an Android device or start an emulator.
5. Click **Run (Shift + F10)**.

---

## 📦 How to Publish to Google Play Store

1. Open the project in **Android Studio**.
2. Click **Build** > **Generate Signed Bundle / APK**.
3. Choose **Android App Bundle (.aab)** and generate your signing key (`.jks`).
4. Log into your [Google Play Console](https://play.google.com/console).
5. Create a new app, fill in your store details, upload your `.aab` file from `app/release/app-release.aab`, and submit for review.

---

## 📞 Contact Information

- **Name**: Talha Mahmood
- **Email**: [talhamahmood1055@gmail.com](mailto:talhamahmood1055@gmail.com)
- **WhatsApp / Phone**: [+92 325 5691055](https://wa.me/923255691055)
- **Location**: Pakistan / Remote Worldwide

---
*© 2026 Talha Mahmood. All rights reserved.*
