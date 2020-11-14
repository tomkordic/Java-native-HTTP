package com.tom.streamlabs.media;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class MediaDetails {

    static final String FILE_NAME_JSON_KEY = "filename";
    static final String VIDEOS_JSON_KEY = "video_streams";
    static final String AUDIO_JSON_KEY = "audio_streams";
    static final String SUBTITLE_JSON_KEY = "subtitle_streams";

    String mFileName;
    ArrayList<VideoDetails> mVideos = new ArrayList<>();
    ArrayList<AudioDetails> mAudios = new ArrayList<>();
    ArrayList<SubtitleDetails> mSubtitles = new ArrayList<>();

    public MediaDetails(String aFileName) {
        mFileName = aFileName;
    }

    public void addSubtitle(SubtitleDetails aSub) {
        mSubtitles.add(aSub);
    }

    public void addVideo(VideoDetails aVid) {
        mVideos.add(aVid);
    }

    public void addAudio(AudioDetails aAud) {
        mAudios.add(aAud);
    }

    public JsonObject toJson() {
        JsonObject lResult = new JsonObject();
        JsonArray lVideoJsonArray = new JsonArray();
        JsonArray lAudioJsonArray = new JsonArray();
        JsonArray lSubtitleJsonArray = new JsonArray();
        for (VideoDetails lVid : mVideos) {
            lVideoJsonArray.add(lVid.toJson());
        }
        for (AudioDetails lAud : mAudios) {
            lAudioJsonArray.add(lAud.toJson());
        }
        for (SubtitleDetails lSub : mSubtitles) {
            lSubtitleJsonArray.add(lSub.toJson());
        }
        lResult.add(VIDEOS_JSON_KEY, lVideoJsonArray);
        lResult.add(AUDIO_JSON_KEY, lAudioJsonArray);
        lResult.add(SUBTITLE_JSON_KEY, lSubtitleJsonArray);
        lResult.addProperty(FILE_NAME_JSON_KEY,  mFileName);
        return lResult;
    }
}
