package kr.co.farmstory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderItems is a Querydsl query type for OrderItems
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderItems extends EntityPathBase<OrderItems> {

    private static final long serialVersionUID = -931142317L;

    public static final QOrderItems orderItems = new QOrderItems("orderItems");

    public final NumberPath<Integer> itemCount = createNumber("itemCount", Integer.class);

    public final NumberPath<Integer> itemDiscount = createNumber("itemDiscount", Integer.class);

    public final NumberPath<Integer> itemNo = createNumber("itemNo", Integer.class);

    public final NumberPath<Integer> itemPrice = createNumber("itemPrice", Integer.class);

    public final NumberPath<Integer> orderNo = createNumber("orderNo", Integer.class);

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public QOrderItems(String variable) {
        super(OrderItems.class, forVariable(variable));
    }

    public QOrderItems(Path<? extends OrderItems> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderItems(PathMetadata metadata) {
        super(OrderItems.class, metadata);
    }

}

