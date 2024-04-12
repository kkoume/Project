package kr.co.farmstory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCarts is a Querydsl query type for Carts
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCarts extends EntityPathBase<Carts> {

    private static final long serialVersionUID = -2057080718L;

    public static final QCarts carts = new QCarts("carts");

    public final NumberPath<Integer> cartNo = createNumber("cartNo", Integer.class);

    public final NumberPath<Integer> cartProdCount = createNumber("cartProdCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> cartProdDate = createDateTime("cartProdDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final StringPath userId = createString("userId");

    public QCarts(String variable) {
        super(Carts.class, forVariable(variable));
    }

    public QCarts(Path<? extends Carts> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCarts(PathMetadata metadata) {
        super(Carts.class, metadata);
    }

}

