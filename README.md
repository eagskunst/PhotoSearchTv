# PhotoSearch

A TV application that shows the feed of Flickr and let's you search photos from the same site.

### Architecture

This applications follows the MVVM + Clean Architecture.

### Compiling

1. Clone the project.
2. In your `local.properties` file, add your Flickr API key like this:

```groovy
sdk.dir = /Users/ your / sdk / path

FLICKR_API_KEY = 1234 _your_api_key
```

3. Run on your desired TV device.

### Contributing

I made this app for practice/professional purposes, but PR's are welcome!

### Libraries and other stuff applied

The only libraries outside the android jetpack ecosystem used were:

1. Retrofit: Network requests.
2. Moshi: JSON parsing.
3. Coroutines: Kotlin threading framework.
4. Coil: Image loading backed with coroutines.
5. DaggerHilt: Dependency injection.

### Check the app!

You could download the .apk file from the release tab.
