package com.sparta.gymspartaprojectbackend.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentUserNotification is a Querydsl query type for PaymentUserNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentUserNotification extends EntityPathBase<PaymentUserNotification> {

    private static final long serialVersionUID = 1609220913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentUserNotification paymentUserNotification = new QPaymentUserNotification("paymentUserNotification");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final com.sparta.gymspartaprojectbackend.payment.entity.QPayment payment;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.gymspartaprojectbackend.user.entity.QUser user;

    public QPaymentUserNotification(String variable) {
        this(PaymentUserNotification.class, forVariable(variable), INITS);
    }

    public QPaymentUserNotification(Path<? extends PaymentUserNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentUserNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentUserNotification(PathMetadata metadata, PathInits inits) {
        this(PaymentUserNotification.class, metadata, inits);
    }

    public QPaymentUserNotification(Class<? extends PaymentUserNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.payment = inits.isInitialized("payment") ? new com.sparta.gymspartaprojectbackend.payment.entity.QPayment(forProperty("payment"), inits.get("payment")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.gymspartaprojectbackend.user.entity.QUser(forProperty("user")) : null;
    }

}

