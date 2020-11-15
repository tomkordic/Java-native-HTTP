cmake_minimum_required(VERSION 3.17)
project(ffmpeg_integration)

set(CMAKE_CXX_STANDARD 14)


if ( FFMPEG_INCLUDE_DIRECTORY AND FFMPEG_LIB_DIRECTORY )
	set( CMAKE_MODULE_PATH ${CMAKE_CURRENT_SOURCE_DIR} )
	find_package( avformat )
	find_package( avcodec )
	find_package( avutil )
	find_package( swscale )
else()
	find_path(AVCODEC_INCLUDE_DIR libavcodec/avcodec.h REQUIRED )
	find_library(AVCODEC_LIBRARY avcodec REQUIRED )
	find_path(AVFORMAT_INCLUDE_DIR libavformat/avformat.h REQUIRED )
	find_library(AVFORMAT_LIBRARY avformat REQUIRED )
	find_path(AVUTIL_INCLUDE_DIR libavutil/avutil.h REQUIRED )
	find_library(AVUTIL_LIBRARY avutil REQUIRED )
	find_path(AVDEVICE_INCLUDE_DIR libavdevice/avdevice.h REQUIRED )
	find_library(AVDEVICE_LIBRARY avdevice REQUIRED )
endif()

find_package( Java )
find_package( Java COMPONENTS Runtime )
find_package( Java COMPONENTS Development )


message("Found javah at: " ${Java_JAVAC_EXECUTABLE})
string(REGEX MATCH "(.*)bin\/javac.*" Java_root ${Java_JAVAC_EXECUTABLE})
message("Found java root at: " ${CMAKE_MATCH_1})

set ( JAVA_HOME
        ${CMAKE_MATCH_1}
)

set(Java_INCLUDE_DIRS
        ${JAVA_HOME}/include
        ${JAVA_HOME}/include/linux
)

set(Java_LIBRARIES
        ${JAVA_HOME}/lib
)

if ( NOT JAVA_HOME )
    message(FATAL_ERROR "Java not found, install Java and make suure it is added to system path !!!")
endif()

set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../../lib)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../../lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../../lib)

include_directories(
        ${Java_INCLUDE_DIRS}
        ${CMAKE_CURRENT_SOURCE_DIR}/jin
        ${Boost_INCLUDE_DIR}
        ${AVFORMAT_INCLUDE_DIR}
        ${AVCODEC_INCLUDE_DIR}
        ${AVUTIL_INCLUDE_DIR}
        ${SWSCALE_INCLUDE_DIR}
)

link_directories(
        ${Java_LIBRARIES}
)

## TODO See how to resolve LD_LIBRARY_PATH set
set(
        LINK_LIBRARIES
        ${CMAKE_THREAD_LIBS_INIT}
        ${AVFORMAT_LIBRARY}
        ${AVCODEC_LIBRARY}
        ${AVUTIL_LIBRARY}
        ${SWSCALE_LIBRARY}
        ${EXTRA_LIBRARIES}
)

set(
        SRC_FILES
        ${CMAKE_CURRENT_SOURCE_DIR}/jni/jni.c
)

add_library(ffmpeg_wrapper SHARED ${SRC_FILES})
##add_executable(ffmpeg_wrapper ${SRC_FILES})

target_link_libraries(
        ffmpeg_wrapper
        ${LINK_LIBRARIES}
)
