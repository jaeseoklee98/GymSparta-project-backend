package com.sparta.gymspartaprojectbackend.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAllNotification is a Querydsl query type for UserAllNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAllNotification extends EntityPathBase<UserAllNotification> {

    private static final long serialVersionUID = -985688810L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAllNotification userAllNotification = new QUserAllNotification("userAllNotification");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    public final QAllNotification allNotification;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath deleted = createBoolean("deleted");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.gymspartaprojectbackend.user.entity.QUser user;

    public QUserAllNotification(String variable) {
        this(UserAllNotification.class, forVariable(variable), INITS);
    }

    public QUserAllNotification(Path<? extends UserAllNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAllNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAllNotification(PathMetadata metadata, PathInits inits) {
        this(UserAllNotification.class, metadata, inits);
    }

    public QUserAllNotification(Class<? extends UserAllNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.allNotification = inits.isInitialized("allNotification") ? new QAllNotification(forProperty("allNotification"), inits.get("allNotification")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.gymspartaprojectbackend.user.entity.QUser(forProperty("user")) : null;
    }

}

