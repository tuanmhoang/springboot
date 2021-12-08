# Getting Started

### General ideas

Need APIs work with Amazon S3. 

**Example scenario:** 

- an employee with accountId is abc123 wants to upload his/her files to S3.
- he wants to list his uploaded files
- he can download the files

### APIs

- upload file: POST `http://localhost:9999/s3/upload/abc123`
- list file: GET `http://localhost:9999/s3/listFiles/abc123`
- download file: GET `http://localhost:9999/s3/downloadFile/abc123/my-cv.pdf`

### Sample response

- upload
```
{
    "message": "OK"
}
```
- list file
```
{
    "message": "OK",
    "fileNames": [
        "abc123/my-cv.pdf",
        "abc123/my-notes.txt"
    ]
}
```
- download file
```
{
    "fileData": "JVBERi0xLjQKMSAwIG9iago8PAovVGl0bGUgKP7/AFgAZQBt....",
    "message": "OK"
    
```

### Notes

- Upload using Amazon S3: https://docs.aws.amazon.com/AmazonS3/latest/userguide/upload-objects.html
- Create/List Amazon S3 objects in sub dir [1]: https://stackoverflow.com/questions/11491304/amazon-web-services-aws-s3-java-create-a-sub-directory-object
- Create/List Amazon S3 objects in sub dir [2]:https://stackoverflow.com/questions/8027265/how-to-list-all-aws-s3-objects-in-a-bucket-using-java
- Put object with input stream: https://stackoverflow.com/questions/8351886/amazons3-putobject-with-inputstream-length-example
- Get object from Amazon S3: https://docs.aws.amazon.com/AmazonS3/latest/userguide/download-objects.html
- Get file type: Use `apache tika`


