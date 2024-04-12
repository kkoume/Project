package kr.co.farmstory.service;

import jakarta.transaction.Transactional;
import kr.co.farmstory.dto.ArticleDTO;
import kr.co.farmstory.dto.FileDTO;
import kr.co.farmstory.entity.Article;
import kr.co.farmstory.repository.ArticleRepository;
import kr.co.farmstory.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    @Value("${file.upload.path}")               // application.yml에서 값을 가져와 Bean에 주입(application.yml에 파일경로 설정 있음)
    private String fileUploadPath;

    public int fileUpload(ArticleDTO articleDTO)  {
         if (fileUploadPath.startsWith("file:")) {
        fileUploadPath =  fileUploadPath.substring("file:".length());
        };
        
        String path = new File(fileUploadPath).getAbsolutePath();  //실제 업로드 할 시스템상의 경로 구하기
        int ano = articleDTO.getNo();
        int count = 0;
        for(MultipartFile mf : articleDTO.getFiles()){
            if(mf.getOriginalFilename() !=null && mf.getOriginalFilename() != ""){
                String oName = mf.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf(".")); //확장자
                String sName = UUID.randomUUID().toString()+ ext;

                log.info("oName : "+oName);
                try{
                    //upload directory에 upload가 됨
                    mf.transferTo(new File(path, sName));

                    FileDTO fileDTO = FileDTO.builder()
                            .ano(ano)
                            .oName(oName)
                            .sName(sName)
                            .build();
                    fileRepository.save(fileDTO.toEntity());
                    count++;
                }catch (IOException e){
                    log.error("fileUpload : "+e.getMessage());
                }
            }
        }
        return count;
    }

    @Transactional
    public ResponseEntity<?> fileDownload(int fno)  {

        // 파일 조회
        kr.co.farmstory.entity.File file = fileRepository.findById(fno).get();

        try {
                if (fileUploadPath.startsWith("file:")) {
        fileUploadPath =  fileUploadPath.substring("file:".length());
        };
            
            Path path = Paths.get(fileUploadPath + file.getSName());
            String contentType = Files.probeContentType(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(file.getOName(), StandardCharsets.UTF_8).build());

            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            Resource resource = new InputStreamResource(Files.newInputStream(path));

            // 파일 다운로드 카운트 업데이트
            file.setDownload(file.getDownload() + 1);
            fileRepository.save(file);

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        }catch (IOException e){
            log.error("fileDownload : " + e.getMessage());
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> fileDownloadCount(int fno)  {

        // 파일 조회
        kr.co.farmstory.entity.File file = fileRepository.findById(fno).get();

        // 다운로드 카운트 Json 생성
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("count", file.getDownload());

        return ResponseEntity.ok().body(resultMap);
    }

    // 여러파일삭제(게시글 삭제)
    public void deleteFiles(int ano){

            if (fileUploadPath.startsWith("file:")) {
        fileUploadPath =  fileUploadPath.substring("file:".length());
        };
        
        String path = new File(fileUploadPath).getAbsolutePath();
        List<kr.co.farmstory.entity.File> files = fileRepository.findFilesByAno(ano);
        for(kr.co.farmstory.entity.File file : files){
            String sName = file.getSName();
            int fno = file.getFno();
            fileRepository.deleteById(fno);

            File deleteFile = new File(fileUploadPath+File.separator+sName);
            if(deleteFile.exists()){
                deleteFile.delete();
            }
        }
    }
}
