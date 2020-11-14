package com.tom.streamlabs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thaiopensource.util.Uri;
import com.tom.streamlabs.media.MediaDetails;
import fi.iki.elonen.NanoFileUpload;
import fi.iki.elonen.NanoHTTPD;
import jnr.ffi.annotations.In;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.System.exit;

public class SlNanoServer extends NanoHTTPD {

    static {
        try {
            System.loadLibrary("ffmpeg_wrapper");
        } catch (Error Err) {
            System.out.println("Failed to load shared library libffmpeg_wraper.so, exiting now !!!");
            exit(-1);
        }
    }

    public static final String MIME_JSON = "application/json";

    static String gStoragePath = "./storage";
    static String gStaticPath = "./static";

    private native MediaDetails getFileDetailsFF(String aFilePath);

    public SlNanoServer(int port) throws IOException {
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    public static void main(String[] args ) throws IOException {
        // TODO do some argument parsing
        File storageDir = new File(gStoragePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        new SlNanoServer(9090);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        if (uri.startsWith("/upload")) {
            return handleFileUploading(session);
        }
        if (uri.startsWith("/list_files")) {
            return listUploadedFiles(session);
        }
        if (uri.startsWith("/file_details")) {
            return showFileDetails(session);
        }
        return fileUploadPage(session);
    }

    Response listUploadedFiles(IHTTPSession session) {
        File storageDir = new File(gStoragePath);
        JsonObject lAvailableFiles = new JsonObject();
        JsonArray filesArray = new JsonArray();
        for (String file : storageDir.list()) {
            filesArray.add(file);
        }
        lAvailableFiles.add("Files", filesArray);
        return newFixedLengthResponse(Response.Status.OK, MIME_JSON, lAvailableFiles.toString());
    }

    Response showFileDetails(IHTTPSession session) {
        String lFileName = null;
        if (session.getUri().indexOf("file_name=") == -1) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_JSON, "");
        }
        lFileName = session.getUri().split("file_name=")[1];
        if (lFileName.contains("&")) {
            lFileName = lFileName.split("&")[0];
        }
        File lFile = new File(gStoragePath + "/" + lFileName);
        if (!lFile.exists()) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_JSON, "");
        }
        MediaDetails lDetails = getFileDetailsFF(gStoragePath + "/" + lFileName);
        return newFixedLengthResponse(Response.Status.OK, MIME_JSON, lDetails.toJson().toString());
    }

    Response fileUploadPage(IHTTPSession session) {
        try {
            Path fileName  = Path.of(gStaticPath + "/" + "index.html");
            return newFixedLengthResponse(Response.Status.OK, MIME_HTML, Files.readString(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT,
                    e.getMessage());
        }
    }

    Response handleFileUploading(IHTTPSession session) {
        try {
            List<FileItem> files
                    = new NanoFileUpload(new DiskFileItemFactory()).parseRequest(session);
            int uploadedCount = 0;
            for (FileItem file : files) {
                try {
                    String fileName = file.getName();
                    byte[] fileContent = file.get();
                    Path destination = Path.of(gStoragePath + "/" + fileName);
                    Files.write(destination, fileContent);
                    uploadedCount++;
                } catch (Exception exception) {
                    // handle
                }
            }
            return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT,
                    "Uploaded files " + uploadedCount + " out of " + files.size());
        } catch (FileUploadException e) {
            throw new IllegalArgumentException("Could not handle files from API request", e);
        }
    }
}
