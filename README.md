# popular-movies (stage 2 submission)
popular movies project app

## About the code
The code uses the following paradigms:
- The architecture used is MVP. One model layer (DataHandler interface) and View and presenters 
for both screens (communicates via interfaces)
- Retrofit is used for remote communication
- Reactive extensions are used to make the app reactive
- Retrofit reactive extension is used for compatibility
- Dagger is used for dependency injection
- Picasso for image loading and caching
- Android architecture components
    - Room ORM for local persistence
    - View Model with MVP

## How to run the app
- Create a `gradle.properties` file in root folder of the project if it doesn't exist already.
- Enter your TMDB api key in `gradle.properties` file like this: `TMDB_API_KEY = 
"YOUR_TMDB_API_KEY"` Don't forget the quotes
- This app uses dagger2 and retrofit2 libraries which heavily depend on annotation processing. To
 run the app, please clean the project and rebuild. **`You may have to repeat this a couple of 
 times`** so Dagger and Retrofit related compile time classes can be generated. Just cleaning the project 
 is not sufficient.

## Features
Apart from the rubric, the app has following additional features
- The api loads 20 videos at a time, when last 2 rows are visible, api loads next batch 
automatically.
- Local persistence using room and view model
- On orientation change, the visible movie is still visible on screen.
- Spinner at the toolbar makes api calls every time the selection changes, this resets the number
 of movies to default 20 videos.
- As many linter issues resolved as possible, some classes that are used by libraries show 
unused, hence left those. Lint rules needs to be updated.

## Pending
- Some lint issues

## Please review these
- MVP with View Model and Room - I know I made it messy, what can I change to improve it?
- MoviesDAO
- Usage of ViewModel