# Java-native-HTTP

Java-native-HTTP is a Java based HTTP server for media file upload, it relies on FFMPEG libraries to to query video/audio details that will be served via HTTP.

## Installation

Currently supporting Ubuntu only. Dependencies are java and ffmpeg libraries.
Install following packages:
```bash
sudo apt-get install openjdk-11-jre-headless openjdk-11-jdk-headless g++ libavcodec-dev libavfilter-dev libavutil-dev libavformat-dev
```
Modify build.gradle file on line 90:
```groovy
//    def.JAVA_HOME="/usr/lib/jvm/java-11-openjdk-amd64/"
```
to set the JAVA_HOME path
```groovy
    def.JAVA_HOME="My java root directory"
```
In case that ffmpeg is not installed using the package manager then ffmpeg include and lib directory need to be specified in build.gradle on line 89 and 90.
```groovy
//    def.FFMPEG_INCLUDE_DIRECTORY="/mnt/7fab2260-fb19-41a7-ac7c-816bab2f3b92/install/ffmpeg_build/include"
//    def.FFMPEG_LIB_DIRECTORY="/mnt/7fab2260-fb19-41a7-ac7c-816bab2f3b92/install/ffmpeg_build/lib"
```
Uncoment these two lines and set the paths accordingly.

## Usage

From the project root directory, run following commands to build and run the server.
```bash
./gradlew build
./gradlew run --args="port number" --info
```
If the project builds correctly and all is good the last command will hang that means that server is running, visit http://your_server_ip:port_passed_as_argument To view the upload page.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
