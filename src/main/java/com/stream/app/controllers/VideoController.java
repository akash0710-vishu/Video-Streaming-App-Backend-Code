package com.stream.app.controllers;

import com.stream.app.entities.Video;
import com.stream.app.playload.CustomMessage;
import com.stream.app.services.VideoService;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
@CrossOrigin("*")
public class VideoController {

    private VideoService videoService;

    public VideoController(VideoService videoService){
        this.videoService = videoService;
    }


    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam("file")MultipartFile file ,
            @RequestParam("title") String title,
            @RequestParam("description") String description
            ){

       Video video = new Video();
       video.setTitle(title);
       video.setDescription(description);
       video.setVideoId(UUID.randomUUID().toString());

       Video savedVideo = videoService.save(video, file);

       if(savedVideo!=null){
           return ResponseEntity
                   .status(HttpStatus.OK)
                   .body(video);
       }else {
           return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(CustomMessage.builder()
                           .message("Video not uploaded")
                           .success(false)
                           .build()
                   );
       }
    }
    @GetMapping
    public List<Video> getAll(){
        return videoService.getAll();
    }
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream(
        @PathVariable String videoId

    ){

        Video video = videoService.get(videoId);

        String contentType = video.getContentType();
        String filePath = video.getFilePath();

        Resource resource= (Resource) new FileSystemResource(filePath);

        if(contentType==null){
            contentType="application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);

    }
}
