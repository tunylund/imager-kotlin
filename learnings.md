No tutorials needed, co pilot chat provides all the example code i need
I'm creating tests and code as i would normally
i might skip testing some obvious functionalities because they feel too trivial to create tests for





fuck off
```kotlin
private fun ensureSuffix(originalFilename: String, contentType: String?): String {
    if (contentType == null) {
        return originalFilename
    } else {
        // cursor here
    }
```
 it just bloody finished it
```kotlin
        val suffix = contentType.split("/").last()
        return if (originalFilename.endsWith(suffix)) {
            originalFilename
        } else {
            "$originalFilename.$suffix"
        }
```

```kotlin
    @Test
    fun `store should make jpeg vs jpg situation consistent`() {
        val mockMultipartFile = MockMultipartFile(
            "test",
            "test",
            "image/jpeg",
            "test data".toByteArray()
        )

        val storedFileName = fileStorageService.store(mockMultipartFile)

        assertEquals("test.jpg", storedFileName)
}
```