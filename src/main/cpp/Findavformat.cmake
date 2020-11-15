# Locate AVFORMAT library
# This module defines
# AVFORMAT_LIBRARY, the name of the library to link against
# AVFORMAT_FOUND, if false, do not try to link to AVFORMAT
# AVFORMAT_INCLUDE_DIR, where to find avformat.h
#

set( AVFORMAT_FOUND "NO" )

find_path( AVFORMAT_INCLUDE_DIR libavformat/avformat.h
  HINTS
  $ENV{AVFORMATDIR}
  PATH_SUFFIXES include 
  PATHS
  ${FFMPEG_INCLUDE_DIRECTORY}
)

message( "AVFORMAT_INCLUDE_DIR is ${AVFORMAT_INCLUDE_DIR}" )

find_library( AVFORMAT_LIBRARY
  NAMES avformat
  HINTS
  $ENV{AVFORMATDIR}
  PATH_SUFFIXES lib64 lib bin
  PATHS
  ${FFMPEG_LIB_DIRECTORY}
)

message( "AVFORMAT_LIBRARY is ${AVFORMAT_LIBRARY}" )

set( AVFORMAT_FOUND "YES" )

message( "AVFORMAT_LIBRARY is ${AVFORMAT_LIBRARY}" )
