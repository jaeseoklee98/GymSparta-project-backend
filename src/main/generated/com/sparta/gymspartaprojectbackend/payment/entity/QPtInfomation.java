package com.sparta.gymspartaprojectbackend.payment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPtInfomation is a Querydsl query type for PtInfomation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPtInfomation extends EntityPathBase<PtInfomation> {

    private static final long serialVersionUID = -1311600422L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPtInfomation ptInfomation = new QPtInfomation("ptInfomation");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isMembership = createBoolean("isMembership");

    public final EnumPath<com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus> paymentStatus = createEnum("paymentStatus", com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus.class);

    public final NumberPath<Double> ptPrice = createNumber("ptPrice", Double.class);

    public final EnumPath<com.sparta.gymspartaprojectbackend.payment.enums.PtTimes> ptTimes = createEnum("ptTimes", com.sparta.gymspartaprojectbackend.payment.enums.PtTimes.class);

    public final com.sparta.gymspartaprojectbackend.trainer.entity.QTrainer trainer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.gymspartaprojectbackend.user.entity.QUser user;

    public QPtInfomation(String variable) {
        this(PtInfomation.class, forVariable(variable), INITS);
    }

    public QPtInfomation(Path<? extends PtInfomation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPtInfomation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPtInfomation(PathMetadata metadata, PathInits inits) {
        this(PtInfomation.class, metadata, inits);
    }

    public QPtInfomation(Class<? extends PtInfomation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.trainer = inits.isInitialized("trainer") ? new com.sparta.gymspartaprojectbackend.trainer.entity.QTrainer(forProperty("trainer"), inits.get("trainer")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.gymspartaprojectbackend.user.entity.QUser(forProperty("user")) : null;
    }

}

