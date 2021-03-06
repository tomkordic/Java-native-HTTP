cmake_minimum_required(VERSION 3.10)
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

message ( STATUS "JAVA_HOME: ${JAVA_HOME}")
if ( NOT JAVA_HOME )

    find_package( JNI )

    if ( NOT JNI_FOUND)
        message ( WARNING "JNI not found, trying alternative method")
        find_package( Java )
        find_package( Java COMPONENTS Runtime )
        find_package( Java COMPONENTS Development )

        message("Found javah at: " ${Java_JAVAC_EXECUTABLE})
        string(REGEX MATCH "(.*)bin\/javac.*" Java_root ${Java_JAVAC_EXECUTABLE})
        message("Found java root at: " ${CMAKE_MATCH_1})

        set ( JAVA_HOME
            ${CMAKE_MATCH_1}
        )

        set(JNI_INCLUDE_DIRS
            ${JAVA_HOME}/include
            ${JAVA_HOME}/include/linux
        )

        set(JNI_LIBRARIES
            ${JAVA_HOME}/lib
        )
    else()
        message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
        message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
    endif()
else()
    file( TO_CMAKE_PATH ${JAVA_HOME}/include JNI_INCLUDE )
    file( TO_CMAKE_PATH ${JAVA_HOME}/include/linux JNI_INCLUDE_LINUX )
    set ( JNI_INCLUDE_DIRS ${JNI_INCLUDE} ${JNI_INCLUDE_LINUX} )
    file( TO_CMAKE_PATH ${JAVA_HOME}/lib JNI_LIBRARIES )
    message ( STATUS "JNI_INCLUDE_DIRS: ${JNI_INCLUDE_DIRS}")
    message ( STATUS "JNI_LIBRARIES: ${JNI_LIBRARIES}")
endif()


set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../../lib)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../../lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../../../lib)

include_directories(
        ${JNI_INCLUDE_DIRS}
        ${CMAKE_CURRENT_SOURCE_DIR}/jin
        ${Boost_INCLUDE_DIR}
        ${AVFORMAT_INCLUDE_DIR}
        ${AVCODEC_INCLUDE_DIR}
        ${AVUTIL_INCLUDE_DIR}
        ${SWSCALE_INCLUDE_DIR}
)

link_directories(
        ${JNI_LIBRARIES}
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
