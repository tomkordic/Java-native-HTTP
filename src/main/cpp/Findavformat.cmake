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
  /mnt/7fab2260-fb19-41a7-ac7c-816bab2f3b92/install/ffmpeg_build/include
)

message( "AVFORMAT_INCLUDE_DIR is ${AVFORMAT_INCLUDE_DIR}" )

find_library( AVFORMAT_LIBRARY
  NAMES avformat
  HINTS
  $ENV{AVFORMATDIR}
  PATH_SUFFIXES lib64 lib bin
  PATHS
  /mnt/7fab2260-fb19-41a7-ac7c-816bab2f3b92/install/ffmpeg_build/lib
)

message( "AVFORMAT_LIBRARY is ${AVFORMAT_LIBRARY}" )

set( AVFORMAT_FOUND "YES" )

message( "AVFORMAT_LIBRARY is ${AVFORMAT_LIBRARY}" )
