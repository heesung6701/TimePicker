[![Release](https://jitpack.io/v/heesung6701/TimePicker.svg)]
(https://jitpack.io/v/heesung6701/TimePicker.svg)


## Setup
Add the JitPack repository in your build.gradle (top level module):
```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And add next dependencies in the build.gradle of the module:
```gradle
dependencies {
    implementation 'com.github.heesung6701:TimePicker:0.0.1'
}
```