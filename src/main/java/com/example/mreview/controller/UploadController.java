package com.example.mreview.controller;

import com.example.mreview.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {
    @Value("${com.example.upload.path}") //application.properties의 변수
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles){
            //이미지 파일만 있는지 확인
            if(uploadFile.getContentType().startsWith("image") == false){
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        for (MultipartFile uploadFile : uploadFiles){
            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf('\\')+1);

            System.out.println("fileName: "+ fileName);
            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath
                    +File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try{
                //원본파일 저장 requestBody에 포함된 하나의 이미지 파일을 서버에 저장
                uploadFile.transferTo(savePath);

                //Thumbnail file name 생성 : file 이름 중간에 s_로 시작하도록
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator
                        + "s_" + uuid + "_" + fileName;

                //Thumbnail file 생성
                File thumbnailFile = new File(thumbnailSaveName);
                // 1-원본파일, 2-생성파일(output), 34-size
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            }catch (IOException e){
                e.printStackTrace();
            }
        }//end for

        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    private String makeFolder(){
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        //make folder
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    @GetMapping("/display") //이미지를 반환하는 rest method
    public ResponseEntity<byte[]> getFile(String fileName, String size){
        ResponseEntity<byte[]> result = null;

        try{
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            System.out.println("fileName: "+ srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);

            // Thumbnail이 아닌 원래 이미지를 가져옴
            if(size != null && size.equals("1")){
                file = new File(file.getParent(), file.getName().substring(2));
            }

            System.out.println("file: "+file);

            HttpHeaders header = new HttpHeaders();

            //MIME 타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/removeFile") //DB내용 삭제가 아니여서 PostMapping인듯
    public ResponseEntity<Boolean> removeFile(String fileName){

        String srcFileName = null;
        try{
            //원본 파일 삭제
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath+File.separator+srcFileName);
            boolean result = file.delete();

            //Thumbnail 파일 삭제
            File thumbnail = new File(file.getParent(), "s_"+file.getName());
            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
