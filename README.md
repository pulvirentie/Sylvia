# Sylvia

Sylvia for JVM enables JVM developers to easily work with [YOOX](https://www.yoox.com) catalog primitives.

## Usage examples

Any instance of `ItemsBuilder` is _country-aware_. That is. You need to provide different instances for different countries/markets.  
For example, if you want to browse the YOOX catalog for the Italian market, you need to build an instance like the following:

```kotlin
val sylvia = ItemsBuilder("it").build()
```

### Get info on a specific item:

Just call the `get` function passing the `id` argument (which represents the product id). An instance of type `com.yoox.net.models.outbound.Item` is returned.

```kotlin
suspend fun loadItem() {
    val sylvia = ItemsBuilder("uk").build()
    val item = sylvia.get("41868153").execute()
    println("you've got a product for brand '${item.brand.name}'")
}
```

Unlike Kotlin, Java lacks native mechanisms to declare "suspensions". Alternatively, you can rely on asynchronous callbacks.  
Here's an example built for Android:

```java
AndroidExecutor.execute(request, new Callback<Item>() {
    @Override
    public void onComplete(Item result) {
        Log.d("Sylvia", String.format("you've got a product for brand '%s'", result.getBrand().getName()));
    }

    @Override
    public void onError(@NonNull Throwable t) {
        // TODO: handle error
    }
});
```

### Search products by department:

The `search` function allows the browsing of YOOX catalog by specifying the department of interest. Available departments are defined by the `com.yoox.net.models.outbound.DepartmentType` enum class and an instance of `com.yoox.net.models.outbound.SearchResults` is returned. The latter is made of the following properties:

* `items` (of type `List<SearchResultItem>`): the list of items for the current page
* `refinements` (of type `List<Refinement>`): the list of available filters for the current search results
* `prices` (of type `Prices`): the currently applied pricing window
* `stats` (of type `SearchStats`): useful data about pagination and cardinality.

For example:

```kotlin
suspend fun searchItems() {
    val sylvia = ItemsBuilder("uk").build()
    val items = sylvia.search(DepartmentType.Men).execute()
    println("you've found ${items.stats.itemCount} items")
}
```

### Working with refinements

You can easily apply filters within your search results.  
The `search` function returns an instance of type `SearchResults`, which contains the `refinements` property of type `List<Refinement>`.  
We provide useful extensions to target any available kind of refinement (`Iterable<Refinement>.colors()`, `Iterable<Refinement>.designers()` and `Iterable<Refinement>.catgories()`). The following example illustrates how to apply a designer filter to previously retrieved search results:

```kotlin
suspend fun filterSearchResults() {
    val sylvia = ItemsBuilder("uk").build()
    // search within men's department
    val items = sylvia.search(DepartmentType.Men).execute()
    // just grab the first filter of kind "designer"
    val designerFilter = items.refinements.designers().first()
    // send a new search request (and apply the above filter as well)
    val filteredItems = sylvia.search(DepartmentType.Men)
        .filterBy(designerFilter)
        .execute()
    println("you've found ${filteredItems.stats.itemCount} items")
}
```

## Try it out

Start coding! Have a look to the [Java sample app](https://github.com/YTech/Sylvia/tree/doc/samplejavaapp) or the [Kotlin one](https://github.com/YTech/Sylvia/tree/doc/samplekotlinapp).

## Supported Platforms

 * JVM

## Getting Started

### Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
        maven { url "https://kotlin.bintray.com/kotlinx" }
    }
}
```

Then, add the library to your module `build.gradle`
```gradle
dependencies {
    implementation 'com.github.YTech.Sylvia:sylvia:0.0.2'
    implementation 'com.github.YTech.Sylvia:rx:0.0.2'               // Optional
    implementation 'com.github.YTech.Sylvia:androidcallback:0.0.2'  // Optional
}
```

## Build

Sylvia is a single jar designed to be easy to deploy anywhere.

```sh
git clone git@github.com:YTech/Sylvia.git
cd Sylvia
```

To build the library, run the following command:

Windows: `gradlew.bat build`

Linux/OSX: `./gradlew build`

