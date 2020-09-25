# Github Trending Repositories

Android demo project which displays the trending Kotlin repositories from Github. The displayed data comes from [https://github.com/trending/kotlin](https://github.com/trending/kotlin) and is made available and fetched from [https://github-trending-api.now.sh/repositories?language=kotlin&since=daily](https://github-trending-api.now.sh/repositories?language=kotlin&since=daily). 

The app has a caching layer which stores the data in a Room database. It also uses Retrofit, RxJava and is built on top of a MVVM architecture.
