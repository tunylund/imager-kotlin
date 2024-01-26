* GET /images/filename
* returns the file
* returns 404 if file not found

show me how to get media content type of a file

> To get the media content type of a file in Kotlin, you can use the probeContentType() method from the java.nio.file.Files class. This method returns the MIME type of the file, or null if the file type is not recognized.  Here's a simple function that takes a file path as a parameter and returns its media content type:

```kotlin
import java.nio.file.Files
import java.nio.file.Paths

fun getContentType(filePath: String): String? {
    val path = Paths.get(filePath)
    return Files.probeContentType(path)
}
```