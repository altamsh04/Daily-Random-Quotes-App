# Daily Random Quotes App

<div align='center'>
  <br>
  <img src="https://github.com/altamsh04/Daily-Random-Quotes-App/assets/84860267/4abc6dd7-fa76-4cc3-876e-0d160e0ff5a1" alt="DailyRandomQuotesAppLogo" width="300">
  <br>
</div>

## Overview

The **Daily Random Quotes App** is an Android application designed to provide users with a daily dose of inspiration through random quotes. The app fetches quotes from an online API and displays them in a user-friendly manner. Users can also set daily notifications to receive quotes at a specific time and manage their favorite quotes.

## Features

- **Random Quotes Fetching**: Fetches random quotes from an online API.
- **Daily Notifications**: Allows users to set daily reminders to receive quotes at a specific time.
- **Favorite Quotes Management**: Users can save their favorite quotes and view them in a separate section.
- **Share quotes**: Users can share quotes on the differnt platform.
- **User-friendly Interface**: Easy-to-navigate interface with a clean design.

## Screenshots

<div align='center'>
  <br>
  <img src="https://github.com/altamsh04/Daily-Random-Quotes-App/assets/84860267/d7f5a4dd-b834-4109-a3f0-db9495248780" alt="DailyRandomQuotesAppUI">
  <br>
</div>

## Installation

1. Clone the repository:
    ```bash
    https://github.com/altamsh04/Daily-Random-Quotes-App.git
    ```
2. Open the project in Android Studio.
3. Build and run the app on an Android device or emulator.

## Usage

1. **Main Screen**: Displays a random quote fetched from the API.
    - Tap the refresh button to fetch a new quote.
    - Tap the heart icon to add a quote to your favorites.
    - Tap the share icon to share quotes on other platforms. 
    
2. **Favorites Screen**: View and manage your favorite quotes.
    - Set a daily reminder to receive a notification with a random quote.
    - Delete the reminder if you no longer want to receive notifications.
    
3. **Splash Screen**: Displays a splash screen for 3 seconds before redirecting to the main screen.

## Code Structure

### Main Components

1. **MainActivity.java**:
    - Displays the main screen with the random quote.
    - Handles user interactions for refreshing quotes and managing favorites.

2. **FavoriteActivity.java**:
    - Displays the favorite quotes saved by the user.
    - Allows users to set and delete daily reminders.

3. **NotificationReceiver.java**:
    - Handles the logic for fetching quotes and displaying notifications.

4. **DatabaseHelper.java**:
    - Manages the SQLite database for storing favorite quotes.

5. **TimerDatabaseHelper.java**:
    - Manages the SQLite database for storing reminder times.

### Database

- **favorites** table: Stores the favorite quotes with `id`, `quote`, and `author` columns.
- **timer** table: Stores the reminder time with `hour` and `minute` columns.

### API

The app uses the **Random Quotes API** to fetch quotes. The API endpoint used is:
```
https://random-quotes-freeapi.vercel.app/api/random
```
The API returns a JSON array with objects containing `quote` and `author` fields.

#### Example API Response:

```json
[
  {
    "id": "23",
    "quote": "Life is what happens when you're busy making other plans.",
    "author": "John Lennon"
  }
]
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
