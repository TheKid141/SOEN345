# ticket reservation app 🎟️

The ticket reservation app is a comprehensive android application that allows users to reserve event tickets. The application was created as part of a project and utilizes java and firebase for backend functionality. In addition to providing a feature-rich interface for users, the application also has secure role-based access control, real-time data synchronization with Firestore, transaction-safe ticket Bookings, and automated email notification capabilities.

## ✨ application features

### customer interface:

* **authentication:** allows Customers to register and securely log into their accounts utilizing Firebase Authentication.
* **global list of active events:** provides a customer viewable global list of all active events along with filtering options based on Date, category, and Location.
* **real-time booking functionality:** utilizes Firestore transactions that are concurrency-safe allowing multiple simultaneous reservations to occur while preventing events from being oversold.
* **booking dashboard:** displays all active reservations within a dedicated dashboard. Users can Cancel any reservation(s) they wish to Cancel.
* **automated email notifications:** sends automated confirmation emails and cancellation emails to registered users upon completion of a successful booking or cancellation request respectively. Uses emailjs REST api.

### administrator interface:

* **administrator login panel:** creates a secure administrator login panel to Manage the application.
* **Manage event functionality:** enables Administrators to create new events, read existing events, update events, and remove events from the system.
* **lifecyle functions:** enables Administrators to Cancel events which hides the event from users and cancels/resets the reservations for said canceled event; restores canceled events; removes permanent deleted events from the system database.
* **custom capacity limit settings:** enables Administrators to set a custom maximum number of attendees allowed per event, or allow no limit at all.

## 🛠️ used technologies

* **programming Language:** java (jdk 17)
* **software development kit (sdk):** Android SDK (minimum version: 24 / maximum version: 36)
* **backend services:** Firebase Authentication, cloud Firestore
* **third party integrations:** emailjs (REST api)
* **Testing frameworks:** junit 5 (unit test), robolectric (component test), Espresso (ui/End-to-End test)
* **Continuous Integration / delivery toolchain:** github actions

## 🚀 running the application

### required tools

* **development environment:** Android Studio (recommended Koala or later)
* **Java Development Kit (jdk):** jdk 17
* **firebase project configuration:** Firebase Project with Authentication enabled (email/password) and Firestore enabled

### setting up the application

#### clone the repository

```bash
git clone https://github.com/yourusername/ticketreservationapp.git
cd ticketreservationapp
```

#### add your firebase configuration file

1. Go to your [Firebase Console](https://console.firebase.google.com/).
2. Click on "select web app".
3. Enter the name of your web app (i.e. "ticketreservationapp").
4. Press next.
5. Copy your google-services.json configuration file url.
6. Replace `` in your project's app/google-services.json file with your actual firebase project id.
7. Download your google-services.json file directly from your Firebase Console.
8. Move the downloaded google-services.json file into your app/ directory.
9. The google-services.json file should be ignored by git. If it isn't ignore it now.
10. Run sync project with gradle files.
11. Build the project via command line:

```bash
./gradlew assembleDebug
```

## 🧪 Testing

All of the required unit tests were written for this project as well as integration tests and End-to-End acceptance tests in order to utilize a robust Testing pyramid approach.

* to run all of your unit and component tests use the following command:

```bash
./gradlew testDebugUnitTest
```

* to run all of your ui/End-to-End tests use the following command:

```bash
./gradlew connectedAndroidTest
```
