**Udacity - Android Developer Nanodegree**
==
### Popular Movies - Stage 2
*Trailers, Reviews, and Favorites*

![alt text](https://github.com/stez/popularmovies-stage2/blob/master/app/src/main/res/mipmap-xhdpi/ic_launcher.png) 

Sample App for the Udacity Android Developer Nanodegree Progam.

Using the `themoviedb.org API` the app will:
- Upon launch, present the user with an grid arrangement of movie posters.
- Allow your user to change sort order via a setting:
    - The sort order can be by most popular, or by top rated
- Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
    - original title
    - movie poster image thumbnail
    - A plot synopsis (called overview in the api)
    - user rating (called vote_average in the api)
    - release date
    
In Stage 2 the details view got more features:
- users will be able to view and play trailers ( either in the youtube app or a web browser)
- users will be able to read reviews of a selected movie
- users will be able to mark a movie as a favorite in the details view by tapping a button(star). 
- the sorting criteria for the main view will include an additional pivot to show favorites collection

In this project I decided to play a little with:

- Content Providers
- Architecture Components
- RxJava for Android
- Dagger2 for Android (v2.12)
- Retrofit
- Picasso
- Butterknife
- Timber
    
Also, to use the *Tthemoviedb.org API* you need an API key.
Place your API key in the `gradle.properties` file such as:

**API_KEY="YourApiKey"**

    
![alt text](https://github.com/stez/popularmovies-stage2/blob/develop/device-main.png)

![alt text](https://github.com/stez/popularmovies-stage2/blob/develop/device-detail.png)

![alt text](https://github.com/stez/popularmovies-stage2/blob/develop/device-detail-land.png)

App Icon taken from <a href="http://www.freepik.com" title="Freepik">Freepik</a> - <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> - licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a>