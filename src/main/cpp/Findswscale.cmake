# Locate SWSCALE library
# This module defines
# SWSCALE_LIBRARY, the name of the library to link against
# SWSCALE_FOUND, if false, do not try to link to SWSCALE
# SWSCALE_INCLUDE_DIR, where to find SWSCALE.h
#

set( SWSCALE_FOUND "NO" )

find_path( SWSCALE_INCLUDE_DIR libswscale/swscale.h
        HINTS
        $ENV{SWSCALEDIR}
        PATH_SUFFIXES include
        PATHS
        /mnt/7fab2260-fb19-41a7-ac7c-816bab2f3b92/install/ffmpeg_build/include
        )

message( "SWSCALE_INCLUDE_DIR is ${SWSCALE_INCLUDE_DIR}" )

find_library( SWSCALE_LIBRARY
        NAMES swscale
        HINTS
        $ENV{SWSCALEDIR}
        PATH_SUFFIXES lib64 lib bin
        PATHS
        /mnt/7fab2260-fb19-41a7-ac7c-816bab2f3b92/install/ffmpeg_build/lib
        )

message( "SWSCALE_LIBRARY is ${SWSCALE_LIBRARY}" )

set( SWSCALE_FOUND "YES" )

message( "SWSCALE_LIBRARY is ${SWSCALE_LIBRARY}" )
