package kr.co.farmstory.service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.farmstory.dto.FileDTO;
import kr.co.farmstory.dto.ProdImageDTO;
import kr.co.farmstory.dto.ProductsDTO;
import kr.co.farmstory.entity.ProdImage;
import kr.co.farmstory.entity.Products;
import kr.co.farmstory.repository.ProdImageRepository;
import kr.co.farmstory.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    // RootConfig Bean 생성/등록
    private final ModelMapper modelMapper;
    private final ProductsRepository productsRepository;
    private final ProdImageRepository prodImageRepository;

    public Products productRegister(ProductsDTO productsDTO) {

        Products products = modelMapper.map(productsDTO, Products.class);


        MultipartFile image1 = productsDTO.getMultImage1();
        MultipartFile image2 = productsDTO.getMultImage2();
        MultipartFile image3 = productsDTO.getMultImage3();

        List<MultipartFile> files = List.of(image1, image2, image3);

        List<ProdImageDTO> uploadedImages = fileUpload(files);
        //image1,2,3 set 해서 sname 넣기.
        if(!uploadedImages .isEmpty()){
            for (int i = 0; i < uploadedImages.size(); i++) {
                ProdImageDTO imageDTO = uploadedImages.get(i);
                if (i == 0) {
                    products.setImage1(imageDTO.getSName());
                } else if (i == 1) {
                    products.setImage2(imageDTO.getSName());
                } else if (i == 2) {
                    products.setImage3(imageDTO.getSName());
                }
            }
        }

        log.info("registerProd....1"+ products);

        Products savedProduct = productsRepository.save(products);
        log.info("registerProd....2" + savedProduct);

        for (ProdImageDTO prodImageDTO : uploadedImages){
            prodImageDTO.setPNo(savedProduct.getProdNo());

            ProdImage prodImage = modelMapper.map(prodImageDTO, ProdImage.class);
            prodImageRepository.save(prodImage);
        }

        return savedProduct;

    }

    public ProductsDTO selectProduct(int prodNo){
        Optional<Products> optProd = productsRepository.findById(prodNo);

        ProductsDTO productsDTO = null;
        if (optProd.isPresent()){
            Products products = optProd.get();

            productsDTO = modelMapper.map(products, ProductsDTO.class);
        }
        return productsDTO;
    }

    public ResponseEntity<?> deleteProducts(HttpServletRequest req, int prodNo){
        Optional<Products> optProducts = productsRepository.findById(prodNo);
        log.info("deleteProdAtService..1:"+optProducts);

        if (optProducts.isPresent()){
            deleteFile(req, prodNo);
            productsRepository.deleteById(prodNo);

            log.info("deleteProdAtService..2:"+prodNo);
            return ResponseEntity
                    .ok()
                    .body(optProducts.get());

        }else {
            log.info("deleteProdAtService..3:");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("not found");
        }

    }

    @Value("${file.upload.path}")
    private  String fileUploadPath;

    public List<ProdImageDTO> fileUpload(List<MultipartFile> files){
        
            if (fileUploadPath.startsWith("file:")) {
        fileUploadPath =  fileUploadPath.substring("file:".length());
        };
        
        String path = new File(fileUploadPath).getAbsolutePath();

        // 이미지 정보 리턴을 위한 리스트
        List<ProdImageDTO> imageDTOS = new ArrayList<>();

        log.info("fileUploadPath..1 : " + path);

        for (int i = 0; i < files.size(); i++) {
            MultipartFile mf = files.get(i);

            if (!mf.isEmpty()) {
                String oName = mf.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf(".")); // 확장자
                String sName = UUID.randomUUID().toString() + ext;

                try {
                    File file = new File(path, sName);

                    if (i == 0) {
                        // 첫 번째 파일에 대해서만 썸네일 생성
                        Thumbnails.of(mf.getInputStream())
                                .size(150, 150) // 썸네일 크기 지정
                                .toFile(file);
                    } else {
                        // 나머지 파일에 대해서는 원본 이미지 그대로 저장
                        mf.transferTo(file);
                    }

                    // 파일 정보 생성(imageDB에 저장될 DTO)
                    ProdImageDTO prodImageDTO = ProdImageDTO.builder()
                            .oName(oName)
                            .sName(sName)
                            .build();

                    imageDTOS.add(prodImageDTO);
                } catch (IOException e) {
                    log.error("Failed to upload file: " + e.getMessage());
                }
            }
        }

        return imageDTOS;
    }

    public void deleteFile(HttpServletRequest req, int prodNo) {

        List<ProdImage> prodImages = prodImageRepository.findBypNo(prodNo);
        log.info("deleteProdAtService..1:" + prodImages);

            if (fileUploadPath.startsWith("file:")) {
        fileUploadPath =  fileUploadPath.substring("file:".length());
        };
        

        for(ProdImage prodImage : prodImages){
            //ProdImageDTO prodImageDTO = modelMapper.map(prodImage, ProdImageDTO.class);

            // 업로드 디렉토리 파일 삭제
            ServletContext ctx = req.getServletContext();
            String path = new File(fileUploadPath).getAbsolutePath();

            // 파일 객체 생성
            //File file = new File(uploadPath + File.separator + prodImageDTO.getSName());
            File file = new File(path + File.separator + prodImage.getSName());

            // 파일 삭제
            if(file.exists()) {
                file.delete();
            }

            prodImageRepository.delete(prodImage);

        }




    }


}
