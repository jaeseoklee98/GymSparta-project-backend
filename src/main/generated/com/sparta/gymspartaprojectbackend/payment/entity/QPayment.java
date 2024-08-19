package com.sparta.gymspartaprojectbackend.payment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPayment is a Querydsl query type for Payment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayment extends EntityPathBase<Payment> {

    private static final long serialVersionUID = 896772902L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPayment payment = new QPayment("payment");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> expiryDate = createDateTime("expiryDate", java.time.LocalDateTime.class);

    public final BooleanPath isMembership = createBoolean("isMembership");

    public final DateTimePath<java.time.LocalDateTime> paymentDate = createDateTime("paymentDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> paymentId = createNumber("paymentId", Long.class);

    public final EnumPath<com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus> paymentStatus = createEnum("paymentStatus", com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus.class);

    public final EnumPath<com.sparta.gymspartaprojectbackend.payment.enums.PaymentType> paymentType = createEnum("paymentType", com.sparta.gymspartaprojectbackend.payment.enums.PaymentType.class);

    public final com.sparta.gymspartaprojectbackend.product.entity.QProduct product;

    public final EnumPath<com.sparta.gymspartaprojectbackend.payment.enums.ProductType> productType = createEnum("productType", com.sparta.gymspartaprojectbackend.payment.enums.ProductType.class);

    public final EnumPath<com.sparta.gymspartaprojectbackend.payment.enums.PtTimes> ptTimes = createEnum("ptTimes", com.sparta.gymspartaprojectbackend.payment.enums.PtTimes.class);

    public final com.sparta.gymspartaprojectbackend.store.entity.QStore store;

    public final com.sparta.gymspartaprojectbackend.trainer.entity.QTrainer trainer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.gymspartaprojectbackend.user.entity.QUser user;

    public QPayment(String variable) {
        this(Payment.class, forVariable(variable), INITS);
    }

    public QPayment(Path<? extends Payment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPayment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPayment(PathMetadata metadata, PathInits inits) {
        this(Payment.class, metadata, inits);
    }

    public QPayment(Class<? extends Payment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.sparta.gymspartaprojectbackend.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
        this.store = inits.isInitialized("store") ? new com.sparta.gymspartaprojectbackend.store.entity.QStore(forProperty("store"), inits.get("store")) : null;
        this.trainer = inits.isInitialized("trainer") ? new com.sparta.gymspartaprojectbackend.trainer.entity.QTrainer(forProperty("trainer"), inits.get("trainer")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.gymspartaprojectbackend.user.entity.QUser(forProperty("user")) : null;
    }

}

