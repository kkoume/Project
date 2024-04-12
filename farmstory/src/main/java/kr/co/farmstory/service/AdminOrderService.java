package kr.co.farmstory.service;

import kr.co.farmstory.dto.*;
import kr.co.farmstory.entity.OrderItems;
import kr.co.farmstory.entity.Orders;
import kr.co.farmstory.entity.Points;
import kr.co.farmstory.entity.Products;
import kr.co.farmstory.repository.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminOrderService {
    private ModelMapper modelMapper;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private OrdersItemsRepository ordersItemsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointsRepository pointsRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;

    //주문목록 조회

    @Transient
    public OrderPageResponseDTO searchs(ProductPageRequestDTO requestDTO){
        List<OrderNotDetailDTO> lists = new ArrayList<>();
        Pageable pageable = requestDTO.getPageable("orderNo");
        Page<Orders> orders = ordersRepository.findAll(pageable);
        int total = ordersRepository.findAll().size();
        log.info("orders 1 : "+orders.toString() );
        for(Orders order : orders){
            OrderNotDetailDTO orderListNotDetail = new OrderNotDetailDTO();
            orderListNotDetail.setOrderNo(order.getOrderNo());
            int d =0;
            if(order.getOrderTotalPrice()<30000) d=5000;
            orderListNotDetail.setDelivery(d);
            orderListNotDetail.setTotalFee(order.getOrderTotalPrice());

            //orderitems와 야매 조인
            List<OrderItems> orderItemsList = ordersItemsRepository.findAllByOrderNo(order.getOrderNo());
            String prodName = productsRepository.findById(orderItemsList.get(0).getProdNo()).get().getProdName();
            orderListNotDetail.setProductName(prodName);
            orderListNotDetail.setOrderDate(order.getOrderDate());
            int totalCount =0;
            for(OrderItems orderItems : orderItemsList){
                totalCount += orderItems.getItemCount();
            }
            orderListNotDetail.setTotalCount(totalCount);

            //userName 야매 조인
            String userName = userRepository.findById(order.getUserId()).get().getName();
            orderListNotDetail.setOrderName(userName);

            lists.add(orderListNotDetail);
        }
        log.info("orders 2 : "+lists.toString() );
        return  OrderPageResponseDTO.builder()
                .productPageRequestDTO(requestDTO)
                .dtoList(lists)
                .total(total)
                                .build();
    }


    @Transient
    public Map<String, Object> forDetail(int orderNo){
        Orders orders = ordersRepository.findById(orderNo).get();
        Map<String, Object> map= new HashMap<>();
        //주문자 정보 + 받으시는 분 완료
        map.put("orders" , orders);

        //주문자 결재내역 포인트만 추가?
        List<Points> points = pointsRepository.findAllByPointDate(orders.getOrderDate());
        int point =0;
        for(Points points1 : points){
            if(points1.getPoint()<0){
                point=points1.getPoint();
            }
        }
        map.put("usedPoint", point);

        //주문 상세 완료
        List<OrderItems> orderItems = ordersItemsRepository.findAllByOrderNo(orderNo);
        List<Products> products  = new ArrayList<>();
        List<String> cates = new ArrayList<>();
        for(OrderItems orderItem : orderItems){
            Products products1 = productsRepository.findById(orderItem.getProdNo()).get();
            products.add(products1);
            cates.add(categoriesRepository.findById(products1.getCateNo()).get().getCateName());
        }
        map.put("orderItems", orderItems);
        map.put("products", products);
        map.put("cates", cates);

        return map;
    }
}

