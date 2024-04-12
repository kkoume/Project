package kr.co.farmstory.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.farmstory.dto.*;
import kr.co.farmstory.service.AdminOrderService;
import kr.co.farmstory.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;

    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping("/admin/index")
    public String index(Model model, ProductPageRequestDTO productPageRequestDTO, UserPageRequestDTO userPageRequestDTO){

        ProductPageResponseDTO productPageResponseDTO = null;
        productPageResponseDTO = adminService.selectProductsForAdmin(productPageRequestDTO);

        log.info("here....1 : "+productPageResponseDTO);
        model.addAttribute(productPageResponseDTO);

        UserPageResponseDTO userPageResponseDTO = null;
        userPageResponseDTO = adminService.selectsUserForAdmin(userPageRequestDTO);
        log.info("here....2 : "+userPageResponseDTO);

        model.addAttribute(userPageResponseDTO);

        model.addAttribute("lists", adminOrderService.searchs(productPageRequestDTO));

        return "/admin/index";
    }

    //productController 로 이동

   //order/list -> orderadmin으로 옮김

    @GetMapping("/admin/user/list")
    public String userList(Model model, UserPageRequestDTO userPageRequestDTO){


        log.info("userList!!!");
        UserPageResponseDTO pageResponseDTO = null;
        pageResponseDTO = adminService.selectsUserForAdmin(userPageRequestDTO);

        model.addAttribute(pageResponseDTO);
        //log.info(pageResponseDTO.toString());
        return "/admin/user/list";
    }

    @GetMapping("/admin/user/register")
    public String userRegister(){
        return "/admin/user/register";
    }

    @PutMapping("/admin/user/modifyGrade")
    public ResponseEntity<?> putGrade(@RequestBody UserDTO userDTO, HttpServletRequest req){
        return adminService.updateUserGrade(userDTO);
    }

    @GetMapping("/admin/user/detail")
    public String userDetail(@RequestParam("uid")String uid, Model model){
        UserDTO userDTO = adminService.selectUserForAdmin(uid);
        model.addAttribute("user", userDTO);
        return "/admin/user/detail";
    }


}
