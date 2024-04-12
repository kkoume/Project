package kr.co.farmstory.controller;

import kr.co.farmstory.dto.OrderNotDetailDTO;
import kr.co.farmstory.dto.ProductPageRequestDTO;
import kr.co.farmstory.repository.OrdersRepository;
import kr.co.farmstory.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderAdminController {
    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping("/admin/order/list")
    public String orderLsit(Model model, ProductPageRequestDTO requestDTO){
        log.info(requestDTO.getPg() + "!!");
        model.addAttribute("lists", adminOrderService.searchs(requestDTO));
        return "/admin/order/list";
    }



    @GetMapping("/admin/order/detail")
    public String orderDetail(int orderNo, Model model){
        Map<String, Object> map = adminOrderService.forDetail(orderNo);
        model.addAttribute("orders", map.get("orders"));
        model.addAttribute("usedPoint", map.get("usedPoint"));
        model.addAttribute("orderItems", map.get("orderItems"));
        model.addAttribute("products", map.get("products"));
        model.addAttribute("cates", map.get("cates"));
        return "/admin/order/detail";
    }
}
