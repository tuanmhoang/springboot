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
- get presigned url to download: GET `http://localhost:9999/s3/download/abc123/presignedurl/personal-img.jpg` 
- get presigned url to upload: GET `http://localhost:9999/s3/upload/abc123/presignedurl/personal-img.jpg`

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

- get presigned url to download
```
{
    "message": "OK",
    "presignedUrl": "https://...jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20211209T044044Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAQ...&X-Amz-Signature=8da1116a2ffff.."
}
```
- get presigned url to upload
```
{
    "message": "OK",
    "presignedUrl": "https://...jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20211209T044044Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAQ...&X-Amz-Signature=8da1116a2ffff.."
}
```

### Notes

- Upload using Amazon S3: https://docs.aws.amazon.com/AmazonS3/latest/userguide/upload-objects.html
- Create/List Amazon S3 objects in sub dir [1]: https://stackoverflow.com/questions/11491304/amazon-web-services-aws-s3-java-create-a-sub-directory-object
- Create/List Amazon S3 objects in sub dir [2]:https://stackoverflow.com/questions/8027265/how-to-list-all-aws-s3-objects-in-a-bucket-using-java
- Put object with input stream: https://stackoverflow.com/questions/8351886/amazons3-putobject-with-inputstream-length-example
- Get object from Amazon S3: https://docs.aws.amazon.com/AmazonS3/latest/userguide/download-objects.html
- Get file type: Use `apache tika`
- Use presigned URL https://docs.aws.amazon.com/AmazonS3/latest/userguide/PresignedUrlUploadObject.html

## Setup for AWS

There are 2 types of users:
- already have permission to work with S3 (for example admin): don't need to do anything
- create a new user from scratch, who does not have permission to work with S3: follow below steps

### Setup Policy

1. IAM > Policies > Create Policy

![image](https://user-images.githubusercontent.com/37680968/147632232-66471d36-afd7-4158-bba8-89adbf852107.png)

2. Choose S3 > JSON > Next

Use this following json

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Resource": "*",
            "Action": "sts:AssumeRole"
        },
        {
            "Effect": "Allow",
            "Action": "s3:*",
            "Resource": "arn:aws:s3:::tuanmhoanguploadfiles/*"
        }
    ]
}
```

Notice that `tuanmhoanguploadfiles` is the bucket that we will upload files

3. Name the policy and create it. For example `S3_upload_to_demo_bucket`

![image](https://user-images.githubusercontent.com/37680968/147634104-59d85def-6c92-4a19-a12a-c3c354dae39f.png)

### Setup groups (optional)

Go to IAM, User Groups, create group `Developers`> Policy `S3_upload_to_demo_bucket` > create group

Reference: https://aws.amazon.com/blogs/security/how-to-restrict-amazon-s3-bucket-access-to-a-specific-iam-role/

### Setup users

Go to IAM > Users > Create user > assign to a group `Developers` > Save access key and secret key

![image](https://user-images.githubusercontent.com/37680968/147636919-736885e7-9076-43ff-878f-b36b334282e2.png)

### Setup S3

1. Go to S3 create bucket named `tuanmhoanguploadfiles`

2. Go to the created bucket, choose tab `Permissions` > block all public access

![image](https://user-images.githubusercontent.com/37680968/147635302-d95f7504-1ff7-4211-b755-38239d75aa1b.png)

### Deploy code to server

#### Setup credentials

Reference: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials.html

**1. Use AWS CLI**

Reference: https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html

After install AWS CLI, config the credentials https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-quickstart.html 

**2. Using credentials**

Reference: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials.html
