# Jasmine

JasmineDK for JVM enables JVM developers to easily work with [YOOX](https://www.yoox.com) catalog primitives.

## Usage examples

Get info on a specific item.

```kotlin
suspend fun loadItem() {
    val jasmine = ItemsBuilder("uk").build()
    val item = jasmine.get("41868153")
    println("you've got a product for brand '${item.brand.name}'")
}
```

## Supported Platforms

 * JVM

## Getting Started

TODO: install (maven, gradle, whatever)

## Build

JasmineDK is a single jar designed to be easy to deploy anywhere.

```sh
git clone git@github.com:YTech/JasmineDK.git
cd JasmineDK
```

To build the library, run the following command:

Windows: `gradlew.bat build`

Linux/OSX: `./gradlew build`

## Contribute

TODO

## Problems

TODO

