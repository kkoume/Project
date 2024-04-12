package kr.co.farmstory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCategories is a Querydsl query type for Categories
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategories extends EntityPathBase<Categories> {

    private static final long serialVersionUID = -370714019L;

    public static final QCategories categories = new QCategories("categories");

    public final StringPath cateName = createString("cateName");

    public final NumberPath<Integer> cateNo = createNumber("cateNo", Integer.class);

    public QCategories(String variable) {
        super(Categories.class, forVariable(variable));
    }

    public QCategories(Path<? extends Categories> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategories(PathMetadata metadata) {
        super(Categories.class, metadata);
    }

}

