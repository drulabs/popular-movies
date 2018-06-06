# popular-movies (stage 1 submission)
popular movies project app (currently in stage 1)

## About the code
The code uses the following paradigms:
- The architecture used is MVP. One model layer (DataHandler interface) and View and presenters 
for both screens (communicates via interfaces)
- Retrofit is used for remote communication
- Reactive extensions are used to make the app reactive
- Retrofit reactive extension is used for compatibility
- Dagger is used for dependency injection
- Picasso for image loading and caching

## How to run the app
- Enter your TMDB api key in `org.drulabs.popularmovies.config.AppConstants.TMDB_API_KEY`. This 
field currently hold some dummy string.
- This app uses dagger2 and retrofit2 libraries which heavily depend on annotation processing. To
 run the app, please clean the project and rebuild. `You may have to repeat this a couple of times` 
 so Dagger and Retrofit related compile time classes can be generated. Just cleaning the project 
 is not sufficient.

## Additional features
Apart from the rubric, the app has following additional features
- The api loads 20 results at a time, this app has a **LOAD MORE** functionaity, which loads 
further results into the recycler view. The load more button gets visible when recycler view 
reaches bottom.
- Used Coordinator layout and Nested scroll view. The Mockup looked like a coordinator layout.
- Spinner at the toolbar makes api calls every time the selection changes, this resets the number
 of movies to default 20 results. More results can be loaded using the **LOAD MORE** feature.
- As many linter issues resolved as possible, some classes that are used by libraries show 
unused, hence left those. Lint rules needs to be updated.

## Pending
- Handling orientation change

## Please review these
- Usage of dagger2. It is an awesome library for dependency injection. Is the way I stitched View
 interface and Activity okay?
- Is the usage of ActivityScope (annotation @ActivityScope) proper? 
- For stage 2, should I replace reactive extensions with live data? or use reative extentions 
with room as both are compatible. 