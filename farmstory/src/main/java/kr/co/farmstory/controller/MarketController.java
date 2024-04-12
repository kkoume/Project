package kr.co.farmstory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.farmstory.dto.*;
import kr.co.farmstory.entity.Points;
import kr.co.farmstory.repository.ProductsRepository;
import kr.co.farmstory.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.imap.protocol.MODSEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.sql.Savepoint;
import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor

public class MarketController {
    @Autowired
    private MarketService marketService;

    //상품 정보 리스트
    @GetMapping("/market/list")
    public  String marketList(Model model , ProductPageRequestDTO pageRequestDTO){
        log.info(pageRequestDTO.getCate()+"!!!");
        if(pageRequestDTO.getCate() == null && pageRequestDTO.getKeyword() == null){
            ProductPageResponseDTO products = marketService.selectProducts(pageRequestDTO);
            model.addAttribute("products", products);
        }else if(pageRequestDTO.getKeyword() == null && pageRequestDTO.getCate() != null){
            log.info(pageRequestDTO.getCate()+"!!");
            ProductPageResponseDTO products = marketService.selectProductsbyCate(pageRequestDTO);
            model.addAttribute("cate", pageRequestDTO.getCate());
            model.addAttribute("products", products);
        }else if(pageRequestDTO.getKeyword() != null && pageRequestDTO.getKeyword() != null){
            log.info("here!!");
            ProductPageResponseDTO products = marketService.selectProductsForAdmin(pageRequestDTO);
            model.addAttribute("keyword", pageRequestDTO.getKeyword());
            model.addAttribute("cate", pageRequestDTO.getCate());
            model.addAttribute("products", products);
        }else{
            ProductPageResponseDTO products = marketService.selectProductsForAdmin(pageRequestDTO);
            model.addAttribute("keyword" , pageRequestDTO.getKeyword());
            model.addAttribute("products", products);
        }
        return "/market/list";
    }



    //상품 상세 리스트
    @GetMapping("/market/view")
    public  String marketView(Model model, @RequestParam("prodNum") int prodNum){
        model.addAttribute("product",marketService.selectProduct(prodNum));
        return "/market/view";
    }


    //장바구니 구현

    //장바구니 페이지 조회
    @GetMapping("/market/cart")
    public  String marketCart(String user , Model model){
        log.info(user+"!!");
        //user에 있는 장바구니 조회
        List<CartsDTO> carts = marketService.selectCartsByUser(user);
        model.addAttribute("carts", carts);
        log.info(carts+"!!");

        //그 상품들 조회
        List<ProductsDTO> products = marketService.selectProductsByCart(carts);
        model.addAttribute("products" , products);
        //카테고리 조회 (아무리 생각해도 비효율적..)
        List<CategoriesDTO> cates = marketService.selectCategoriesByProduct(products);
        model.addAttribute("cates", cates);

        return "/market/cart";
    }


    //장바구니 넣기
    @ResponseBody
    @PostMapping("/market/cart")
    public ResponseEntity marketCart(@RequestBody Map<String, String> map){
        int no = Integer.parseInt(map.get("prodNo"));
        int count = Integer.parseInt(map.get("count"));
        String id = map.get("userId");
        CartsDTO cart = CartsDTO.builder()
                                .userId(id)
                                .prodNo(no)
                                .cartProdCount(count)
                                .build();
        return marketService.insertCarts(cart);
    }

    //장바구니 삭제하기
    @ResponseBody
    @PutMapping("/market/cart/delete")
    public ResponseEntity marketCartDelete(@RequestBody Map<String, List<Integer>> map){
        log.info(map+"카트 삭제!!");
        List<Integer> cartNos = map.get("list");
        log.info(cartNos+"!!");
        return  marketService.deleteCarts(cartNos);
    }

    //결제 페이지 구현

    //결제 페이지 조회 (장바구니 이용하여 )
    @PostMapping("/market/order")
    public String marketBuy(@RequestParam("cartNo") List<Integer> lists , @RequestParam("user") String userId, Model model) {
        log.info("user!" + userId);
        Set<Integer> set = new LinkedHashSet<>(lists);
        List<Integer> cartsNo = new ArrayList<>(set);

        List<CartsDTO> carts = marketService.selectCartsByCartNo(cartsNo);
        model.addAttribute("carts", carts);
        //그 상품들 조회
        List<ProductsDTO> products = marketService.selectProductsByCart(carts);
        model.addAttribute("products" , products);
        //카테고리 조회 (아무리 생각해도 비효율적..)
        List<CategoriesDTO> cates = marketService.selectCategoriesByProduct(products);
        model.addAttribute("cates", cates);
        model.addAttribute("user", marketService.selectUserByUid(userId));

        return "/market/order";
    }

    //최종 결제 구현 (장바구니 삭제 + 포인트 수정 + orderer + oderItems 넣기, 상품 재고량 변경)
    @ResponseBody
    @PutMapping("/market/order")
    public ResponseEntity marketOrder(@RequestBody OrdersDTO ordersDTO){
       int number = marketService.insertOrers(ordersDTO);
        Map<String, Integer> map = new HashMap<>();
        map.put("result",number );
        return ResponseEntity.ok().body(map);
    };

    //orderItems + 재고량 없애기
    @ResponseBody
    @PutMapping("/market/orderItems")
    public ResponseEntity marketOrderItems(@RequestBody Map<String, List<Integer>> map){
        int orderNo = map.get("no").get(0);
        List<Integer> prodNos = map.get("prodNosList");
        List<Integer> itemCounts = map.get("itemCounts");
        log.info(prodNos.toString());
        log.info(itemCounts.toString());
        log.info(orderNo+"!!!!");
        marketService.insertOrderitems(prodNos, itemCounts, orderNo);
        Map<String, String> map2 = new HashMap<>();
        map2.put("result", "success" );
        return ResponseEntity.ok().body(map2);
    };

    //point없애기
    @ResponseBody
    @PutMapping("/market/updatePoint")
    public ResponseEntity updatePoint(@RequestBody Map<String, String> map){
        String uid = map.get("userId");
        int point = Integer.parseInt(map.get("deletePoint"));

        int savePoint = Integer.parseInt(map.get("savePoint"));
        marketService.updatePoint(uid, savePoint-point);
        //포인트 테이블 수정
        marketService.updatePointList(uid, point, savePoint);
        Map<String, String> map2 = new HashMap<>();
        map2.put("result", "success" );
        return ResponseEntity.ok().body(map2);
    };

    @ResponseBody
    @PutMapping("/market/saveOnlyPoint")
    public ResponseEntity saveOnlyPoint(@RequestBody Map<String, String> map){
        String uid = map.get("userId");
        int savePoint = Integer.parseInt(map.get("savePoint"));
        marketService.updatePoint(uid, savePoint);

        //포인트 테이블 수정
        marketService.updateOnlyPointPlus(uid, savePoint);
        Map<String, String> map2 = new HashMap<>();
        map2.put("result", "success" );
        return ResponseEntity.ok().body(map2);
    };

    //장바구니에서 삭제하기 (기존 메소드 재활용)


    //장바구니 x 바로 결제 이용
    @GetMapping ("/market/buyDirect")
    public String buyDirect(@RequestParam("prodNumber") int prodNo, @RequestParam("user") String userId,
                            @RequestParam("countNo") int countNo, Model model) {
        
        //상품 조회
        ProductsDTO product = marketService.selectProduct(prodNo);
        model.addAttribute("product", product);
        //수량 넣기
        model.addAttribute("countNo", countNo);
        //카테고리 조회 
        model.addAttribute("cate", marketService.selectCategoryByProduct(product));
        //사용자 조회
        model.addAttribute("user", marketService.selectUserByUid(userId));

        return "/market/buyDirect";
    }


}
