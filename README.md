# Imager-Kotlin

_This project was a study of:_
* What is the experience of using Github Co-Pilot as an aid to convert a service from javascript to kotlin?
* How does Co-Pilot support rusty SpringBoot developer to get back up to speed?
* How does Co-Pilot support learning a new language - Kotlin
* What is the impact of using Co-Pilot as part of solo development workflow.
* How small is the effort of converting a service from javascript to kotlin with Co-Pilot as an assistant?

Premise:
* The original service was written in JavaScript several years ago.
* I'm rusty with Java and SpringBoot and I've never written Kotlin.
* Co-Pilot had access to the entire workspace and was able to gather insights from the existing Kotlin code.

## Outcome and learnings:

### My output quality suffered due to the speed and convenience of code generation  
TDD Workflow breaks when Code Assistant tools are used as part of the workflow.
As a developer begins to type a function, Co-Pilot will suggest a complete solution, which is very easy to accept as is.
The generated code tends to be nearly complete in functionality and results in the developer skipping tests for some scenarios.

Instead of the TDD `Red`-`Green`-`Refacor` workflow, Code-Assistants push the workflow to become more like:

1. Generate the first test
1. Generate an implementation
1. Look for missing tests and generate them
1. Refactor and go back to `#3`

Even when realizing and being aware of the risk to missing tests - I managed to miss crucial tests.
It was just too easy to skip on thoroughness of the test suite.

> [!Tip]
> Teams that use Code Assistant need extra effort in place to assure quality of the delivery.
> It would be good to have quality controls in the pipeline that assure that test coverage is high enough.
> Exploratory testing also becomes even more important to catch bugs.


### My awareness of the available APIs and knowledge of how they operate is smaller than I expected  
If I didn't have Co-Pilot as a help - I would spend more time with the library documentations.
As a developer, it is imperative to have a mental map of what apis and functionalities are available
in the main libraries of the project. Knowing what Java standard libraries and SpringBoot libraries provide is crucial
in order to know what good looks like and what possible directions there are
for the code the built towards.

As a result of having Co-Pilot in the workflow, I became much lazier in reading API documentations.
Co-Pilot helped me get the actual work done quickly, but the side-effect is that I don't actually
feel as comfortable as I want with the libraries I used and that harms my ability to direct the future of the work.

> [!Tip]
> Implementation is only a part of a team's outcomes. The learning the team makes during the work is an equally valuable
> outcome. Teams that use Code Assistants will get the job done quickly, but they need to ensure learning is cultivated
> and achieved as part of the work.


### The speed of getting the job done was improved by Co-Pilot

I got the job done. I got it done really quickly with the help of Co-Pilot. At times it
felt like the LLM was reading my mind. I wasn't creating anything spectacularly new,
which means Co-Pilot had easy time in suggesting code completions that just worked and were a good fit.

I had mixed experiences when asking about the specifics of the Image libraries - SCRImage and OpenCV.
Clearly Co-Pilot had less knowledge about them, however it was able to help me at times - just not
as much as with SpringBoot and Kotlin.

> [!Tip]
> Delivery speed is greatly improved with the help of Code Assistants. So much so that I would advocate for using
> Code Assistants in a team.


### Do Code-Assistants kill Creativity?

_"Code Assistants steal our jobs"_, _"Where's the creativity when you just generate code"_ - Random fears from the internet

It didn't feel like I could "just generate" the code. It still required a developer to drive the implementation and make
sure everything makes sense and clicks together - let alone follows good design practices. Outputting the code just
become a lot faster, that's all.


### I feel like I'm back up to speed with SpringBoot
Co-Pilot was very good at providing good examples of code that filled the right purpose,
and gave a decent direction to follow. As I knew SpringBoot from before already, the output of
Co-Pilot acted as a good refresher of my knowledge.


### I don't feel like I know Kotlin yet
This is due to the nature of SpringBoot applications. Most functionalities are
set up with J2EE and Spring annotations instead of code. As a result all SpringBoot applications look pretty much the same
whether they are written in Kotlin or Java. In order to learn Kotlin, I should create a pure
Kotlin project.


### is the effort vs result worth it?  
It Depends. I wouldn't recommend converting an existing service from one language to another just for the sake of it.
The work is certainly faster with Code Assistants than without, but the result can still contain new bugs, and the developers could
spend the same time doing something more valuable. Only if there is significant tech debt in the previous solution, it
would become worth the effort to do the conversion.






## examples

_The following are some samples of the prompts and the respective answers from Co-Pilot_

### Co-Pilot helps orient to the new language and libraries

_It's been a while since I've written SpringBoot and explored Java apis. I needed
to prime my brain and orient it for java in order to do the work._

<details>
<summary>show me how to get media content type of a file</summary>  

> To get the media content type of a file in Kotlin, you can use the probeContentType() method from the
> java.nio.file.Files class. This method returns the MIME type of the file, or null if the file type is not recognized.
> Here's a simple function that takes a file path as a parameter and returns its media content type:

```kotlin
import java.nio.file.Files
import java.nio.file.Paths

fun getContentType(filePath: String): String? {
    val path = Paths.get(filePath)
    return Files.probeContentType(path)
}
```

</details>

<details>
<summary>show me how to get the content-type of a file in a post request using springboot and kotlin</summary>

> In Spring Boot with Kotlin, you can get the content type of a file from a POST request by using the MultipartFile interface.
> Here's how you can do it:  First, you need to modify your uploadImage function in the ImagesController class to accept a MultipartFile parameter.
> The getContentType method of MultipartFile can be used to retrieve the content type of the file.  Here's the updated uploadImage function:

```kotlin
import org.springframework.web.multipart.MultipartFile

// ...

@RequestMapping(path=["/images"], method = [RequestMethod.POST], headers = ["content-type=multipart/form-data"])
fun uploadImage(@RequestParam("file") file: MultipartFile): String {
    val contentType = file.contentType
    println("Content Type: $contentType")

    // Continue with your logic here...

    return "Hello"
}
```
> in this code, @RequestParam("file") file: MultipartFile indicates that the POST request should include a file with
> the parameter name "file". The contentType property of MultipartFile gives the content type of the uploaded file.

_Without Co-Pilot, I would've probably looked into SpringBoot documentation and googled for file upload examples.
However with this prompt, I had my most important question answered immediately
and I was able to implement the code with ease._

</details>

<details>
<summary>show me how to read the uploadDir from application properties</summary>

> In Spring Boot, you can read properties from the application.properties file using the @Value annotation.
> First, add the uploadDir property to your application.properties file:

```kotlin
uploadDir=/path/to/your/upload/directory
```

> Then, you can read the uploadDir property in your FileStorageService class using the @Value annotation. Here's how you can do it:

```kotlin
@Value("\${uploadDir}")
private lateinit var uploadDir: String
```

> In this code, `@Value("\${uploadDir}") private lateinit var uploadDir: String` reads the uploadDir property from the
> application.properties file. The store function then uses this property to determine the upload directory.

_Oh yes of course, the `@Value` annotation_

</details>

<details>
<summary>show me how to validate uploaded file type on a multipart form request</summary>

> in Spring Boot with Kotlin, you can validate the type of an uploaded file in a multipart form request by checking the contentType property of the MultipartFile object. Here's how you can do it:  First, you need to modify your uploadImage function in the ImagesController class to check the contentType of the MultipartFile parameter. If the contentType is not what you expect, you can return an error response.  Here's the updated uploadImage function:

```kotlin
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class ImagesController {

    @GetMapping("/images")
    fun getImage(): String {
        return "Hello"
    }

    @RequestMapping(path=["/images"], method = [RequestMethod.POST], headers = ["content-type=multipart/form-data"])
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val allowedContentTypes = listOf("image/jpeg", "image/png", "image/gif")

        if (file.contentType !in allowedContentTypes) {
            return ResponseEntity("Unsupported file type. Please upload a JPEG, PNG, or GIF image.", HttpStatus.BAD_REQUEST)
        }

        // Continue with your logic here...

        return ResponseEntity("Image uploaded successfully.", HttpStatus.OK)
    }
}
```

> In this code, allowedContentTypes is a list of the MIME types that you want to allow. The if statement checks if the contentType of the uploaded file is in the allowedContentTypes list. If it's not, the function returns a BAD_REQUEST response with an error message. If the contentType is allowed, the function continues with your logic and returns an OK response when done.

</details>

<details>
<summary>show me how to validate maximum request size of a multipart post request</summary>

> In Spring Boot with Kotlin, you can validate the maximum request size of a multipart POST request by using the setMaxFileSize and setMaxRequestSize methods of the MultipartConfigElement class. These methods allow you to set the maximum file size and the maximum request size respectively.  First, you need to create a MultipartConfigElement bean in your configuration class and set the maximum file size and request size. Here's how you can do it:

```kotlin
import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.MultipartConfigElement

@Configuration
class MultipartConfig {

    @Bean
    fun multipartConfigElement(): MultipartConfigElement {
        val factory = MultipartConfigFactory()
        factory.setMaxFileSize("10MB") // Max file size. Change it according to your needs.
        factory.setMaxRequestSize("100MB") // Max request size. Change it according to your needs.
        return factory.createMultipartConfig()
    }
}
```

> In this code, setMaxFileSize("10MB") sets the maximum file size to 10MB and setMaxRequestSize("100MB") sets the maximum request size to 100MB. You can change these values according to your needs.  If a client tries to upload a file or make a request that exceeds these limits, Spring Boot will automatically reject the request and return a MaxUploadSizeExceededException. You can handle this exception to return a custom error message to the client. Here's how you can do it:

```kotlin
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MaxUploadSizeExceededException
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(response: HttpServletResponse) {
        response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "File size exceeds limit!")
    }
}
```

> In this code, @ExceptionHandler(MaxUploadSizeExceededException::class) indicates that the handleMaxUploadSizeExceededException function should be called when a MaxUploadSizeExceededException is thrown. The function sends a SC_REQUEST_ENTITY_TOO_LARGE (413) error response with a custom error message.

_Since it's just configuration, I decided to set these values in the application.properties file instead of using the given example._

</details>


<details>
<summary>show me how to invoke a FileStorage service class to handle the actual file storing</summary>

> To handle the actual file storing, you can create a FileStorageService class with a storeFile method. This method will
> take a MultipartFile as a parameter and handle the actual file storing. Here's an example of how you can create the
> FileStorageService class:

```kotlin
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService {

    private val uploadDir: Path = Paths.get("uploads") // replace with your upload directory

    fun storeFile(file: MultipartFile): String {
        val fileName = file.originalFilename!!

        val targetLocation: Path = uploadDir.resolve(fileName)
        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

        return fileName
    }
}
```

> In this code, storeFile method copies the file to the uploadDir directory. If a file with the same name already exists,
> it will be replaced (StandardCopyOption.REPLACE_EXISTING).  Then, you can modify your uploadImage function in the
> ImagesController class to use the FileStorageService to store the file. You can do this by autowiring the
> FileStorageService and calling the storeFile method with the MultipartFile parameter.  Here's how you can do it:

```kotlin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class ImagesController {

    @Autowired
    lateinit var fileStorageService: FileStorageService

    @GetMapping("/images")
    fun getImage(): String {
        return "Hello"
    }

    @RequestMapping(path=["/images"], method = [RequestMethod.POST], headers = ["content-type=multipart/form-data"])
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val allowedContentTypes = listOf("image/jpeg", "image/png", "image/gif")

        if (file.contentType !in allowedContentTypes) {
            return ResponseEntity("Unsupported file type. Please upload a JPEG, PNG, or GIF image.", HttpStatus.BAD_REQUEST)
        }

        val fileName = fileStorageService.storeFile(file)

        return ResponseEntity("File uploaded successfully: $fileName", HttpStatus.OK)
    }
}
```

> In this code, @Autowired lateinit var fileStorageService: FileStorageService autowires the FileStorageService. In the
> uploadImage function, val fileName = fileStorageService.storeFile(file) calls the storeFile method of FileStorageService
> to store the file and get the file name. The function then returns a success message with the file name.

_Co-pilot gave me a decent example on what the result would look like. Controller calls a Service to do the actual work.
However in this case, I would not pass MultiPartFile as a parameter to the service since MultiPartFile is all about HTTP.
The controller should be responsible for all things HTTP, the service should be responsible of the business logic.
However this is a good starting point._

_I also quite like how well Co-Pilot is using the existing code in the project and all its examples fit really well into
the code style._

</details>

### Co-Pilot helps generate tests

<details>
<summary>Please provide me an api test written in kotlin for the following REST api endpoint: POST request /images path
The request should contain an image file The response status for a successful request is 200 ok</summary>

> Sure, here is a test case for the POST request to the /images endpoint. This test case uses the TestRestTemplate to
> send the request and checks the response status code. It also sends an image file as part of the request.

```kotlin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap

@Test
fun `should return 200 for valid image upload`() {
    val headers = HttpHeaders().apply {
        contentType = MediaType.MULTIPART_FORM_DATA
    }

    val resource = ClassPathResource("test.jpg") // replace with your image file path
    val body = LinkedMultiValueMap<String, Any>().apply {
        add("file", resource)
    }

    val requestEntity = HttpEntity(body, headers)
    val response = restTemplate.exchange("/images", HttpMethod.POST, requestEntity, String::class.java)

    assertEquals(HttpStatus.OK, response.statusCode)
}
```

_This kicked off my test setup. I was not aware of which library is the latest and greatest for kotlin and springboot, but this got me started down a
good path. Co-Pilot directed me to use TestRestTemplate for http-api tests, which was good enough of a choice._

</details>

<details>
<summary>please provide a unit test for the FileStorageService class</summary>

> Sure, here's a basic unit test for the FileStorageService class. This test will check if the store method correctly
> stores a file and returns the file name.  We'll use the MockMultipartFile class to create a mock MultipartFile for
> testing. We'll also use the Files class to check if the file was correctly stored.

```kotlin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.mock.web.MockMultipartFile
import java.nio.file.Files
import java.nio.file.Path

class FileStorageServiceTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `store should correctly store a file and return the file name`() {
        // Arrange
        val fileStorageService = FileStorageService()
        val originalFileName = "test.txt"
        val mockMultipartFile = MockMultipartFile(
            "file",
            originalFileName,
            "text/plain",
            "test data".toByteArray()
        )

        // Act
        val storedFileName = fileStorageService.store(mockMultipartFile)

        // Assert
        assertEquals(originalFileName, storedFileName)
        assertTrue(Files.exists(tempDir.resolve(originalFileName)))
    }
}
```

> In this test, @TempDir lateinit var tempDir: Path creates a temporary directory for testing. The store should correctly
> store a file and return the file name test creates a FileStorageService and a MockMultipartFile. It then calls the
> store method of FileStorageService with the MockMultipartFile and checks if the returned file name is correct and if
> the file was correctly stored.  Please note that you need to update the FileStorageService class to accept the upload
> directory as a constructor parameter. This allows you to pass the temporary directory for testing. Here's how you can do it:

```kotlin
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService(private val uploadDir: Path) {

    fun store(file: MultipartFile): String {
        val fileName = file.originalFilename!!

        val targetLocation: Path = uploadDir.resolve(fileName)
        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

        return fileName
    }
}
```

> In this code, class FileStorageService(private val uploadDir: Path) adds uploadDir as a constructor parameter.
> You can pass the upload directory when creating a FileStorageService.

_Co-Pilot provided useful annotation to generate a temp directory. It also directed towards using Dependency Injection_ 

_I found myself not doing TDD very well. Co-Pilot is generating a lot more code than I am anticipating. In TDD fashion,
I would have to change a test first in order to create more logic, however since I'm copy pasting a lot of code, I end up
missing out on tests and I have to navigate back to the test suite to see if there are any tests missing._

_This also means that my tests are not guiding the design of the code. Whatever the LLM is using as a base of its
learnings is guiding my design as well._

</details>


<details>
<summary>how thorough is the test suite for this service?</summary>

> The test suite for the CropService seems to cover two main scenarios:
> 1. The crop should resize the given image file and create a new image test
     > case checks if the cropmage method correctly resizes a PNG image and creates
     > a new image with the expected dimensions.
> 2. The should resize jpg as well test case checks if the croplmage method
     > correctly resizes a JPG image and creates a new image with the expected
     > dimensions.
>
> However, it seems like the test suite could be more thorough. For instance,
> it does not appear to test what happens when the croplmage method is given
> an invalid file name, or when the cropping parameters are invalid. It also
> does not seem to test the behavior of the croplmage method when it is given
> a file of a type other than PNG or JPG.

_I did exploratory testing only to realize there was a bug in the system. At this
point of time, CropServiceTest looked like this:_

```kotlin
@Test
fun `crop should resize the given image file and create a new image`() ...

@Test
fun `should resize jpg as well`() ...
```

_I hadn't been doing TDD which means I naturally missed out on a lot of tests.
There are only two tests and they test almost exactly the same thing simply checking that
the service support both jpg and png files._

_The answer Co-pilot gave is good - it points at missing scenarios and the tests were
easily added with Co-Pilot's help._

```kotlin
@Test
fun `should throw an exception if the original file does not exist`()
@Test
fun `should overwrite the file if it already exists`()
```


</details>

<details>
<summary>most tests are created with just tab-completion</summary>

_It is quite the feeling to just type this:_
```kotlin
@Test
fun 'GET
```

_and get a perfectly valid suggestion of code that you were thinking of writing._
```kotlin
@Test
fun 'GET should return 404 if the image does not exist () {
    val response = restTemplate.getForEntity("/images/does-not-exist.png", ByteArray: :class.java)
    assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
}
```

_simply pressing tab, the test was created in a couple of seconds from the initial thought._

</details>




### Co-Pilot helps understand Kotlin and SpringBoot

<details>
<summary>what does the !! operator do in kotlin</summary>

> In Kotlin, the !! operator is used to assert that an expression is not null. If the expression is null, it will throw
> a NullPointerException.  For example, in the store function of your FileStorageService class:

```kotlin
val fileName = file.originalFilename!!
```

> The !! operator is used to assert that file.originalFilename is not null. If file.originalFilename is null, a
> NullPointerException will be thrown.

</details>

<details>
<summary>how can i run this async job in the background</summary>

> In Spring Boot, you can use the @Async annotation to run methods asynchronously
> in a separate thread. However, to use @Async, you need to enable it in your
> Spring Boot application by adding the @EnableAsync annotation in your main
> class or any configuration class.

> First, enable async processing in your Spring Boot application:

```kotlin
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class Application
```

> Then, you can annotate the enqueueResizelmage method with @Async:

```kotlin
import org.springframework.scheduling.annotation.Async

@Async
fun enqueueResizeImage (filename: String, resizeParams: ResizeService. ResizeParams) {
// Your existing code here
｝
```

> This will cause Spring Boot to run the enqueueResizelmage method in a separate
> thread, allowing the main thread to continue processing other requests.

_What I really wanted to learn was to understand better what the relationship between
Kotlin co-routines and Async background jobs is. Of course the above example was a perfectly
valid answer to my question and I was able to achieve the job at hand. I also realized at this
point that a SpringBoot application is a very poor candidate for learning Kotlin. Everything felt
very much like SpringBoot because that is the nature of SpringBoot apps._

_In order to actually learn Kotlin, i would have to create a pure Kotlin project._

</details>

### Co-Pilot helps navigate libraries

<details>
<summary>why does opency report an assertion failure from this line of code?</summary>

> The line of code you're referring to is `Improc.resize(image, destimage,
> destSize)`. An assertion failure in OpenCV usually means that some precondition
> for a function call is not met.

> In the case of Imgproc.resize, it could be due to one of the following reasons:
> 1. The source image (image) is empty or not loaded correctly. You can check this by calling image.empty. If it returns true, then the image is not loaded correctly.
> 2. The destination image (destimage) might not have the correct type or size. The destination image should have the same type as the source image and the size you want to resize to.
   In your code, you're creating destimage with image.type and destSize, which seems correct. However, if image is not loaded correctly, image.type( could be incorrect.
> 3. The destination size (destSize) is invalid. It should be a valid Size object with non-negative width and height.
   To debug this issue, you should check the above conditions. Here's how you can do it in Kotlin:

> To debug this issue, you should check the above conditions. Here's how you can do it in Kotlin:

```kotlin
if (image.empty()) {
    println("Source image is empty")
} else {
    println("Source image is loaded correctly")
}
if (destImage.type() != image.type()) {
    println("Destination image type is incorrect")
} else { 
    println("Destination image type is correct")
}
if (destSize.width < 0 || destSize.height < 0) { 
    println("Destination size is invalid")
} else {
    println("Destination size is full")
}
```

_We've all been there, you have a crazy library that you need to bang your head against
until you figure out how to operate it. In this case OpenCV library is native machine code
that is invoked from java bindings. In order for the library to operate, it has internal
assertions on method calls that validate the given parameters._

_Turns out the `image` was indeed an empty parameter even though it was loaded by other opencv method calls,
I would have expected to get `null` or an exception if an image fails to load but instead the following method call
had an assertion to guard against an empty image parameter. This was confusing to me._

_With Co-Pilot's help, I didn't need to bang my head much and I got up to speed with how
the library operates faster than I normally would._


</details>
