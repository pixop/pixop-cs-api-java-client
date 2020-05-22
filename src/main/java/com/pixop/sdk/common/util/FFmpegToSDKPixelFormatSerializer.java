package com.pixop.sdk.common.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pixop.sdk.services.videos.model.VideoPixelFormat;

public class FFmpegToSDKPixelFormatSerializer extends JsonSerializer<String> {
    private final static Pattern DIGITS_PATTERN = Pattern.compile("\\d+");

    @Override
    public void serialize(final String _ffmpegPixelFormatName,
                          final JsonGenerator _jsonGenerator,
                          final SerializerProvider _serializerProvider) throws IOException {
        VideoPixelFormat.ChromaSubsampling chromaSubsampling = null;

        if (_ffmpegPixelFormatName.startsWith("yuvj") || _ffmpegPixelFormatName.startsWith("uyvy") || _ffmpegPixelFormatName.startsWith("yuva")) {
            chromaSubsampling = toChromaSubsampling(_ffmpegPixelFormatName.substring(4, 7), _ffmpegPixelFormatName);
        } else if (_ffmpegPixelFormatName.startsWith("yuv")) {
            chromaSubsampling = toChromaSubsampling(_ffmpegPixelFormatName.substring(3, 6), _ffmpegPixelFormatName);
        }

        _jsonGenerator.writeObject(new VideoPixelFormat(_ffmpegPixelFormatName, chromaSubsampling));
    }

    private static VideoPixelFormat.ChromaSubsampling toChromaSubsampling(final String _subsampling, final String _ffmpegPixelFormatName) {
        // convert yuv420p to the chroma subsampling representation consumed by REST clients
        final String[] parts = _ffmpegPixelFormatName.split("p");
        final String formattedSubsampling = _subsampling.charAt(0) + ":" + _subsampling.charAt(1) + ":" + _subsampling.charAt(2);

        if (parts.length == 1) {
            return new VideoPixelFormat.ChromaSubsampling(formattedSubsampling, 8);
        }

        final Matcher matcher = DIGITS_PATTERN.matcher(parts[1]);

        if (!matcher.find()) {
            // assume 8 bit - we couldn't find any digits
            return new VideoPixelFormat.ChromaSubsampling(formattedSubsampling, 8);
        }

        return new VideoPixelFormat.ChromaSubsampling(formattedSubsampling, Integer.parseInt(matcher.toMatchResult().group()));
    }
}
