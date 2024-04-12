package kr.co.farmstory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPoints is a Querydsl query type for Points
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPoints extends EntityPathBase<Points> {

    private static final long serialVersionUID = 1039841700L;

    public static final QPoints points = new QPoints("points");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> pointDate = createDateTime("pointDate", java.time.LocalDateTime.class);

    public final StringPath pointDesc = createString("pointDesc");

    public final NumberPath<Integer> pointNo = createNumber("pointNo", Integer.class);

    public final StringPath userId = createString("userId");

    public QPoints(String variable) {
        super(Points.class, forVariable(variable));
    }

    public QPoints(Path<? extends Points> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPoints(PathMetadata metadata) {
        super(Points.class, metadata);
    }

}

