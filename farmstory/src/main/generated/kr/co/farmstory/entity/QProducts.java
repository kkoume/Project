package kr.co.farmstory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProducts is a Querydsl query type for Products
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProducts extends EntityPathBase<Products> {

    private static final long serialVersionUID = 1385562597L;

    public static final QProducts products = new QProducts("products");

    public final NumberPath<Integer> cateNo = createNumber("cateNo", Integer.class);

    public final NumberPath<Integer> delivery = createNumber("delivery", Integer.class);

    public final StringPath etc = createString("etc");

    public final StringPath image1 = createString("image1");

    public final StringPath image2 = createString("image2");

    public final StringPath image3 = createString("image3");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Integer> prodDiscount = createNumber("prodDiscount", Integer.class);

    public final StringPath prodName = createString("prodName");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final NumberPath<Integer> prodPrice = createNumber("prodPrice", Integer.class);

    public final NumberPath<Integer> prodSold = createNumber("prodSold", Integer.class);

    public final NumberPath<Integer> prodStock = createNumber("prodStock", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> RegProdDate = createDateTime("RegProdDate", java.time.LocalDateTime.class);

    public QProducts(String variable) {
        super(Products.class, forVariable(variable));
    }

    public QProducts(Path<? extends Products> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProducts(PathMetadata metadata) {
        super(Products.class, metadata);
    }

}

