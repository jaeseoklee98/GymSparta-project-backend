package com.sparta.gymspartaprojectbackend.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = -1299227642L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> expiryDate = createDateTime("expiryDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final DateTimePath<java.time.LocalDateTime> paymentDate = createDateTime("paymentDate", java.time.LocalDateTime.class);

    public final EnumPath<com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus> paymentStatus = createEnum("paymentStatus", com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus.class);

    public final EnumPath<com.sparta.gymspartaprojectbackend.payment.enums.PaymentType> paymentType = createEnum("paymentType", com.sparta.gymspartaprojectbackend.payment.enums.PaymentType.class);

    public final NumberPath<Double> productPrice = createNumber("productPrice", Double.class);

    public final EnumPath<com.sparta.gymspartaprojectbackend.payment.enums.PtTimes> ptTimes = createEnum("ptTimes", com.sparta.gymspartaprojectbackend.payment.enums.PtTimes.class);

    public final com.sparta.gymspartaprojectbackend.trainer.entity.QTrainer trainer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.gymspartaprojectbackend.user.entity.QUser user;

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.trainer = inits.isInitialized("trainer") ? new com.sparta.gymspartaprojectbackend.trainer.entity.QTrainer(forProperty("trainer"), inits.get("trainer")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.gymspartaprojectbackend.user.entity.QUser(forProperty("user")) : null;
    }

}

