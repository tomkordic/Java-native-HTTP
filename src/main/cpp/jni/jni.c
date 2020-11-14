
#include <jni.h>
#include "stdio.h"

#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavfilter/buffersink.h>
#include <libavfilter/buffersrc.h>
#include <libavutil/opt.h>
#include <libavutil/pixdesc.h>


jobject createNewMediaDetails(
            JNIEnv *env,
            jstring aFilePath
) {
    return createJavaObject(env, "com/tom/streamlabs/media/MediaDetails", "(Ljava/lang/String;)V", aFilePath);
}

jobject createNewVideoDetails(
            JNIEnv *env,
            int aWidth,
            int aHeight,
            int aBitRate,
            char *aProfile,
            char *aCodec
) {
    jclass lClass = (*env)->FindClass(env, "com/tom/streamlabs/media/VideoDetails");
    jmethodID lConstructor = (*env)->GetMethodID(env, lClass, "<init>", "(IIILjava/lang/String;Ljava/lang/String;)V");
    return (*env)->NewObject(
                env,
                lClass,
                lConstructor,
                aWidth,
                aHeight,
                aBitRate,
                (*env)->NewStringUTF(env, aProfile),
                (*env)->NewStringUTF(env, aCodec)
                );
}

jobject createNewAudioDetails(
            JNIEnv *env,
            int aSampleRate,
            int aChannels,
            int aBitrate,
            char *aProfile,
            char *aCodec
) {
    jclass lClass = (*env)->FindClass(env, "com/tom/streamlabs/media/AudioDetails");
    jmethodID lConstructor = (*env)->GetMethodID(env, lClass, "<init>", "(IIILjava/lang/String;Ljava/lang/String;)V");
    return (*env)->NewObject(
                env,
                lClass,
                lConstructor,
                aSampleRate,
                aChannels,
                aBitrate,
                (*env)->NewStringUTF(env, aProfile),
                (*env)->NewStringUTF(env, aCodec)
                );
}

void addVideoDetailsToMediaDetails(JNIEnv *env, jobject aMediaDetails, jobject aVideoDetails) {
    jclass lClass = (*env)->FindClass(env, "com/tom/streamlabs/media/MediaDetails");
    jmethodID lAddVideoMethodId = (*env)->GetMethodID(env, lClass, "addVideo",
                "(Lcom/tom/streamlabs/media/VideoDetails;)V");
    (*env)->CallVoidMethod(env, aMediaDetails, lAddVideoMethodId, aVideoDetails);
}

void addAudioDetailsToMediaDetails(JNIEnv *env, jobject aMediaDetails, jobject aAudioDetails) {
    jclass lClass = (*env)->FindClass(env, "com/tom/streamlabs/media/MediaDetails");
    jmethodID lAddAudioMethodId = (*env)->GetMethodID(env, lClass, "addAudio",
                "(Lcom/tom/streamlabs/media/AudioDetails;)V");
    (*env)->CallVoidMethod(env, aMediaDetails, lAddAudioMethodId, aAudioDetails);
}

JNIEXPORT jobject JNICALL Java_com_tom_streamlabs_SlNanoServer_getFileDetailsFF
    (JNIEnv *env, jobject obj, jstring aFilePath) {

    const char *lFileUrl = (*env)->GetStringUTFChars(env, aFilePath, 0);
    AVStream *lAudioStream;
    AVStream *lVideoStream;
    AVFormatContext *lInputFormatContext = avformat_alloc_context();

    int lResult = avformat_open_input(&(lInputFormatContext), lFileUrl, 0, 0);
    if (lResult < 0) {
        printf("Could not open input url, error: %s", lFileUrl);
        goto fail;
    }
    printf("Looking for stream info ...");
    lResult = avformat_find_stream_info(lInputFormatContext, 0);
    if (lResult < 0) {
        printf("Failed to retrieve input stream information");
        goto fail;
    }

    jobject lMediaDetails = createNewMediaDetails(env, aFilePath);

    for (int i = 0; i < lInputFormatContext->nb_streams; i++) {
        if (lInputFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            lAudioStream = lInputFormatContext->streams[i];
            AVCodec *lCodec = avcodec_find_decoder(lAudioStream->codecpar->codec_id);
            const char *lProfileName = lCodec->profiles->name;
            const char *lCodecName = lCodec->name;
            jobject lAudioDetails = createNewAudioDetails(env,
                    lAudioStream->codecpar->sample_rate, lAudioStream->codecpar->channels,
                    lAudioStream->codecpar->bit_rate,
                    lCodecName, lProfileName);
            addAudioDetailsToMediaDetails(env, lMediaDetails, lAudioDetails);
        }
        if (lInputFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            lVideoStream = lInputFormatContext->streams[i];
            AVCodec *lCodec = avcodec_find_decoder(lVideoStream->codecpar->codec_id);
            const char *lProfileName = lCodec->profiles->name;
            const char *lCodecName = lCodec->name;
            jobject lVideoDetails = createNewVideoDetails(env,
                    lVideoStream->codecpar->width, lVideoStream->codecpar->height,
                    lVideoStream->codecpar->bit_rate, lCodecName, lProfileName);
            addVideoDetailsToMediaDetails(env, lMediaDetails, lVideoDetails);
        }
    }
    if (lAudioStream == NULL & lVideoStream == NULL) {
        printf("No playable stream found");
        goto fail;
    }
    return lMediaDetails;
fail:
    (*env)->ReleaseStringUTFChars(env, aFilePath, lFileUrl);
    return NULL;
}