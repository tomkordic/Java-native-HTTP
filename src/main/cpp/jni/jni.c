
#include <jni.h>

#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavfilter/buffersink.h>
#include <libavfilter/buffersrc.h>
#include <libavutil/opt.h>
#include <libavutil/pixdesc.h>

void test(const char *lFileUrl);

int main(int argc, char *argv[]) {
    test("/home/tom/Downloads/google_glass.ts");
//    test("rtsp://admin:Hubsai@54321@office.hubsai.com:554");
    return 0;
}

void test(const char *lFileUrl) {
    AVStream *lAudioStream;
    AVStream *lVideoStream;
    AVFormatContext *lInputFormatContext;

    int lResult = avformat_open_input(&(lInputFormatContext), lFileUrl, 0, 0);
    if (lResult < 0) {
        printf("Could not open input url, error: %s", lFileUrl);
        return;
    }
    printf("Looking for stream info ...");
    lResult = avformat_find_stream_info(lInputFormatContext, 0);
    if (lResult < 0) {
        printf("Failed to retrieve input stream information");
        return;
    }
    for (int i = 0; i < lInputFormatContext->nb_streams; i++) {
        if (lInputFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            lAudioStream = lInputFormatContext->streams[i];
        }
        if (lInputFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            lVideoStream = lInputFormatContext->streams[i];
        }
    }
    if (lAudioStream == NULL & lVideoStream == NULL) {
        printf("No playable stream found");
        return;
    }

    char msg[300];
    sprintf(msg, "Video resolution: (%dx%d), sample rate: %d",
            lVideoStream->codecpar->width, lVideoStream->codecpar->height, lAudioStream->codecpar->sample_rate);
}


JNIEXPORT jstring JNICALL Java_com_tom_streamlabs_SlNanoServer_getFileDetailsFF
    (JNIEnv *env, jobject obj, jstring aFilePath) {

    const char *lFileUrl = (*env)->GetStringUTFChars(env, aFilePath, 0);
    AVStream *lAudioStream;
    AVStream *lVideoStream;
    AVFormatContext *lInputFormatContext;

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
    for (int i = 0; i < lInputFormatContext->nb_streams; i++) {
        if (lInputFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            lAudioStream = lInputFormatContext->streams[i];
        }
        if (lInputFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            lVideoStream = lInputFormatContext->streams[i];
        }
    }
    if (lAudioStream == NULL & lVideoStream == NULL) {
        printf("No playable stream found");
        goto fail;
    }

    char msg[300];
    sprintf(msg, "Video resolution: (%dx%d), sample rate: %d",
        lVideoStream->codecpar->width, lVideoStream->codecpar->height, lAudioStream->codecpar->sample_rate);
    // TODO return full object
    return (*env)->NewStringUTF(env, msg);
fail:
    (*env)->ReleaseStringUTFChars(env, aFilePath, lFileUrl);
    return NULL;
}