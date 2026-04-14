# Ticket Reservation App 🎟️

The Ticket Reservation App is a comprehensive Android application that allows users to reserve event tickets. The application was created as part of the SOEN345 project and utilizes Java and Firebase for backend functionality. In addition to providing a feature-rich interface for users, the application also has secure role-based access control, real-time data synchronization with Firestore, transaction-safe ticket bookings, and automated email notification capabilities.

## ✨ Application Features

### Customer Interface:

* **Authentication:** Allows customers to register and securely log into their accounts utilizing Firebase Authentication.
* **Global List of Active Events:** Provides a customer-viewable global list of all active events along with filtering options based on date, category, and location.
* **Real-Time Booking Functionality:** Utilizes Firestore transactions that are concurrency-safe, allowing multiple simultaneous reservations to occur while preventing events from being oversold.
* **Booking Dashboard:** Displays all active reservations within a dedicated dashboard. Users can cancel any reservation(s) they wish.
* **Automated Email Notifications:** Sends automated confirmation and cancellation emails to registered users upon completion of a successful booking or cancellation request, respectively. Uses the EmailJS REST API.

### Administrator Interface:

* **Administrator Login Panel:** Creates a secure administrator login panel to manage the application. (Default credentials: `admin` / `admin123`).
* **Manage Event Functionality:** Enables administrators to create new events, read existing events, update events, and remove events from the system.
* **Lifecycle Functions:** Enables administrators to cancel events (which hides the event from users and cancels/resets the reservations for said canceled event), restore canceled events, and permanently remove deleted events from the system database.
* **Custom Capacity Limit Settings:** Enables administrators to set a custom maximum number of attendees allowed per event, or allow no limit at all.

## 🏗️ Architecture & Design

* **MVVM Architecture:** The application strictly follows the Model-View-ViewModel pattern to separate UI logic from business logic. UI Activities observe `LiveData` emitted by ViewModels (`EventViewModel`, `ReservationViewModel`).
* **Repository Pattern:** Firestore database interactions are abstracted into dedicated repository classes (`EventRepository`, `ReservationRepository`), centralizing network calls and data mapping.
* **NoSQL Denormalization:** Event details (Title, Location, Category, Date) are duplicated within Reservation documents to optimize Firestore read operations, allowing the "My Reservations" dashboard to load with a single query.

## 🛠️ Used Technologies

* **Programming Language:** Java (JDK 17)
* **Software Development Kit (SDK):** Android SDK (Minimum version: 24 / Maximum version: 36)
* **Backend Services:** Firebase Authentication, Cloud Firestore
* **Third-Party Integrations:** EmailJS (REST API)
* **Testing Frameworks:** JUnit 5 (Unit test), Robolectric (Component test), Espresso (UI/End-to-End test)
* **Continuous Integration / Delivery Toolchain:** GitHub Actions

## 🚀 Running the Application

### Required Tools

* **Development Environment:** Android Studio (Recommended: Koala or later)
* **Java Development Kit (JDK):** JDK 17
* **Firebase Project Configuration:** Firebase Project with Authentication enabled (Email/Password) and Firestore enabled.
* **EmailJS Account:** Required for confirmation notifications.

### Setting Up the Application

#### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/ticketreservationapp.git
cd ticketreservationapp
```

#### 2. Add Your Firebase Configuration File

Go to your Firebase Console.

Click on Add App and select the Android icon.

Enter your application's package name (e.g., com.example.ticketreservationapp).

Click Register app and download the google-services.json file.

Move the downloaded google-services.json file into your project's app/ directory.

Ensure the google-services.json file is ignored by Git in your .gitignore file.

#### 3. Configure EmailJS API Keys

The application uses EmailJS to send transactional emails. You must replace the hardcoded API keys with your own to enable email functionality.

Create a free account at EmailJS.

Add an Email Service and note your service_id.

Create two Email Templates (one for Confirmations, one for Cancellations) and note their template_ids.

Locate your Public Key (user_id) in the Account settings.

Open src/main/java/com/example/ticketreservationapp/EventListActivity.java and locate the sendConfirmationEmail method. Replace the following strings with your credentials:

```java
"\"service_id\":\"YOUR_SERVICE_ID\","
"\"template_id\":\"YOUR_CONFIRMATION_TEMPLATE_ID\","
"\"user_id\":\"YOUR_PUBLIC_KEY\","
```

Open src/main/java/com/example/ticketreservationapp/MyReservationsActivity.java and locate the sendCancellationEmail method. Update the strings with your credentials:

```java
"\"service_id\":\"YOUR_SERVICE_ID\","
"\"template_id\":\"YOUR_CANCELLATION_TEMPLATE_ID\","
"\"user_id\":\"YOUR_PUBLIC_KEY\","
```

#### 4. Build and Run

Sync the project with Gradle files in Android Studio.

Build the project via command line (or press Run in Android Studio):

```bash
./gradlew assembleDebug
```

## 🧪 Testing

All of the required unit tests were written for this project, as well as integration tests and end-to-end acceptance tests, utilizing a robust testing pyramid approach.

To run all of your unit and component tests, use the following command:

```bash
./gradlew testDebugUnitTest
```

To run all of your UI/End-to-End tests, use the following command (requires an active emulator or connected device):

```bash
./gradlew connectedAndroidTest
```

To generate a combined Jacoco test coverage report, use the following command:

```bash
./gradlew jacocoCombinedTestReport
```

The generated HTML report can be found at:

```
app/build/reports/jacoco/jacocoCombinedTestReport/html/index.html
```
