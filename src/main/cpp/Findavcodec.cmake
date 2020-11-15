# Locate AVCODEC library
# This module defines
# AVCODEC_LIBRARY, the name of the library to link against
# AVCODEC_FOUND, if false, do not try to link to AVCODEC
# AVCODEC_INCLUDE_DIR, where to find avcodec.h
#

set( AVCODEC_FOUND "NO" )

find_path( AVCODEC_INCLUDE_DIR libavcodec/avcodec.h
  HINTS
  $ENV{AVCODECDIR}
  PATH_SUFFIXES include 
  PATHS
  ${FFMPEG_INCLUDE_DIRECTORY}
)

message( "AVCODEC_INCLUDE_DIR is ${AVCODEC_INCLUDE_DIR}" )

find_library( AVCODEC_LIBRARY
  NAMES avcodec
  HINTS
  $ENV{AVCODECDIR}
  PATH_SUFFIXES lib64 lib bin
  PATHS
  ${FFMPEG_LIB_DIRECTORY}
)

message( "AVCODEC_LIBRARY is ${AVCODEC_LIBRARY}" )

set( AVCODEC_FOUND "YES" )

message( "AVCODEC_LIBRARY is ${AVCODEC_LIBRARY}" )
