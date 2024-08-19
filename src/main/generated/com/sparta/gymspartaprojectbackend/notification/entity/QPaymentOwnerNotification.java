package com.sparta.gymspartaprojectbackend.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentOwnerNotification is a Querydsl query type for PaymentOwnerNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentOwnerNotification extends EntityPathBase<PaymentOwnerNotification> {

    private static final long serialVersionUID = -665427005L;

    public static final QPaymentOwnerNotification paymentOwnerNotification = new QPaymentOwnerNotification("paymentOwnerNotification");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPaymentOwnerNotification(String variable) {
        super(PaymentOwnerNotification.class, forVariable(variable));
    }

    public QPaymentOwnerNotification(Path<? extends PaymentOwnerNotification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentOwnerNotification(PathMetadata metadata) {
        super(PaymentOwnerNotification.class, metadata);
    }

}

