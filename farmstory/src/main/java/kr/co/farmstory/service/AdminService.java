package kr.co.farmstory.service;

import com.querydsl.core.Tuple;
import kr.co.farmstory.dto.*;
import kr.co.farmstory.dto.ProductPageResponseDTO;
import kr.co.farmstory.entity.Products;
import kr.co.farmstory.entity.User;
import kr.co.farmstory.repository.CategoriesRepository;
import kr.co.farmstory.repository.ProductsRepository;
import kr.co.farmstory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ProductPageResponseDTO selectProductsForAdmin(ProductPageRequestDTO pageRequestDTO) {
       /* Pageable pageable = productPageRequestDTO.getPageable("prodNo");
        Page<Tuple> pageProducts = productsRepository.selectProducts(productPageRequestDTO , pageable);
        log.info("selectAllProd...:" + pageProducts);

        List<ProductsDTO> dtoList = pageProducts.getContent().stream()
                .map(tuple -> {
                    Products products = tuple.get(0 ,Products.class);
                    String cateName = tuple.get(1, String.class);
                    products.setCateName(cateName);
                    return modelMapper.map(products, ProductsDTO.class);
                })
                .toList();
        log.info("selectAllProd...2:" + dtoList);

        int total = (int) pageProducts.getTotalElements();

        return ProductPageResponseDTO.builder()
                .productPageRequestDTO(productPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
        */

        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageArticle = productsRepository.selectProducts(pageRequestDTO , pageable);
        log.info(pageArticle.getContent().toString()+"!!");
        List<ProductsDTO> dtoList = pageArticle.getContent().stream()
                                                .map(tuple -> {
                                                    Products products = tuple.get(0 ,Products.class);
                                                    String cateName = tuple.get(1, String.class);
                                                    products.setCateName(cateName);
                                                    return modelMapper.map(products, ProductsDTO.class);
                                                })
                                                .toList();
        log.info(dtoList+" dto! !!");
        int total = (int) pageArticle.getTotalElements();
        return ProductPageResponseDTO.builder()
                .productPageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public ProductPageResponseDTO searchProductsForAdmin(ProductPageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageArticle = productsRepository.searchProducts(pageRequestDTO , pageable);
        log.info(pageArticle.getContent().toString()+"!!");

        List<ProductsDTO> dtoList = pageArticle.getContent().stream()
                                                .map(tuple -> {
                                                    Products products = tuple.get(0 ,Products.class);
                                                    String cateName = tuple.get(1, String.class);
                                                    products.setCateName(cateName);
                                                    return modelMapper.map(products, ProductsDTO.class);
                                                })
                                                .toList();
        log.info(dtoList+" dto! !!");
        int total = (int) pageArticle.getTotalElements();
        return ProductPageResponseDTO.builder()
                .productPageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public UserPageResponseDTO selectsUserForAdmin(UserPageRequestDTO userPageRequestDTO){

        Pageable pageable = userPageRequestDTO.getPageable();
        Page<Tuple> pageUsers = userRepository.selectsUsers(userPageRequestDTO, pageable);

        //log.info("selectUsers....1: "+ pageUsers);

        List<UserDTO> dtoList = pageUsers.getContent().stream()
                                    .map(tuple ->
                                            {
                                                User user = tuple.get(0, User.class);
                                                Integer totalPrice = tuple.get(1, Integer.class);

                                                int totalPriceValue = (totalPrice != null) ? totalPrice : 0;
                                                user.setTotalPrice(totalPriceValue);

                                                return modelMapper.map(user, UserDTO.class);
                                                }
                                            )
                                            .toList();
        //log.info("selectUsers....2:" + dtoList);
        int total = (int) pageUsers.getTotalElements();

        //log.info("selectUsers....3:" + total);

        return UserPageResponseDTO.builder()
                .userPageRequestDTO(userPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }


    public UserDTO selectUserForAdmin(String uid) {
        // uid에 해당하는 사용자의 정보를 페이징해서 조회합니다.
        Tuple tuple = userRepository.selectUser(uid);
        log.info("selectUser....1: "+ tuple);

        // 튜플을 UserDTO로 변환하는 작업을 수행합니다.
        User user = tuple.get(0, User.class);
        Integer totalPrice = tuple.get(1, Integer.class);
        int totalPriceValue = (totalPrice != null) ? totalPrice : 0;
        user.setTotalPrice(totalPriceValue);

        return modelMapper.map(user, UserDTO.class);
    }

    public ResponseEntity<?> updateUserGrade(UserDTO userDTO){
        log.info("updateGrade...1:"+ userDTO);

        Optional<User> optUser = userRepository.findById(userDTO.getUid());

        if(optUser.isPresent()){
            log.info("updateGrade.....2");

            User user = optUser.get();

            user.setGrade(userDTO.getGrade());
            log.info("updateGrade....3 :" + user);
            User modifyUser = userRepository.save(user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(modifyUser);
        }else {
            log.info("deleteComment.....2");

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("not found");
        }
    }


}
