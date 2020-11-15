# Locate SWRESAMPLE library
# This module defines
# SWRESAMPLE_LIBRARY, the name of the library to link against
# SWRESAMPLE_FOUND, if false, do not try to link to SWRESAMPLE
# SWRESAMPLE_INCLUDE_DIR, where to find SWRESAMPLE.h
#

set( SWRESAMPLE_FOUND "NO" )

find_path( SWRESAMPLE_INCLUDE_DIR libswresample/swresample.h
        HINTS
        $ENV{SWRESAMPLEDIR}
        PATH_SUFFIXES include
        PATHS
        ${FFMPEG_INCLUDE_DIRECTORY}
        )

message( "SWRESAMPLE_INCLUDE_DIR is ${SWRESAMPLE_INCLUDE_DIR}" )

find_library( SWRESAMPLE_LIBRARY
        NAMES swresample
        HINTS
        $ENV{SWRESAMPLEDIR}
        PATH_SUFFIXES lib64 lib bin
        PATHS
        ${FFMPEG_LIB_DIRECTORY}
        )

message( "SWRESAMPLE_LIBRARY is ${SWRESAMPLE_LIBRARY}" )

set( SWRESAMPLE_FOUND "YES" )

message( "SWRESAMPLE_LIBRARY is ${SWRESAMPLE_LIBRARY}" )

