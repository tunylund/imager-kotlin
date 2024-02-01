POST upload an image
```bash
curl -v -H "Content-Type: multipart/form-data" -F "file=@Downloads/image.png" localhost:8080/images
```

PUT crop an image
```bash
curl -v -XPUT -H "Content-Type: application/json" localhost:8080/images/image.png -d '{"x":100,"y":300,"width":200,"height":100}'
```

GET download an image
```bash
curl -v -XGET localhost:8080/images/image.png --output image.png && open image.png
```

GET queue an image resize or download it if available
```bash
curl -v -XGET localhost:8080/images/image.png\?resizeParams=200x200ftrue --output image.png && open image.png
```
