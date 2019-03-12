# Sylvia

Sylvia for JVM enables JVM developers to easily work with [YOOX](https://www.yoox.com) catalog primitives.

## Usage examples

Get info on a specific item.

```kotlin
suspend fun loadItem() {
    val sylvia = ItemsBuilder("uk").build()
    val item = sylvia.get("41868153")
    println("you've got a product for brand '${item.brand.name}'")
}
```

## Supported Platforms

 * JVM

## Getting Started

TODO: install (maven, gradle, whatever)

## Build

Sylvia is a single jar designed to be easy to deploy anywhere.

```sh
git clone git@github.com:YTech/Sylvia.git
cd Sylvia
```

To build the library, run the following command:

Windows: `gradlew.bat build`

Linux/OSX: `./gradlew build`

## Contribute

TODO

## Problems

TODO

