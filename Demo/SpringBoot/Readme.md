## Translator Service
The application translates the DOC file to PDF using REST APIs.

## Steps to Setup

**Requirements**

***1. Java 11***

***2. Maven***

***3. MS Office***

***4. Apache Kafka***
**Note:**

Application requires windows operating system and MS Office should be installed, because it needs MS office 
routine to convert DOC file to PDF.


**1. Specify the file uploads directory**

Open `src/main/resources/application.properties` file and change the property `file.upload-dir` to the path where you want the uploaded files to be stored.

```
file.upload-dir=E:\\pdf\\
```

**2. Run the app using maven**

```bash
cd spring-boot-file-upload-download-rest-api-example
mvn spring-boot:run
```

That's it! The application can be accessed at `http://localhost:8080`.

You may also package the application in the form of a jar and then run the jar file like so -

```bash
mvn clean package
java -jar target/file-demo-0.0.1-SNAPSHOT.jar
```