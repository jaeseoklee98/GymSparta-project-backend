package com.sparta.gymspartaprojectbackend.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserNotification is a Querydsl query type for UserNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserNotification extends EntityPathBase<UserNotification> {

    private static final long serialVersionUID = 1783429441L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserNotification userNotification = new QUserNotification("userNotification");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final com.sparta.gymspartaprojectbackend.user.entity.QUser user;

    public QUserNotification(String variable) {
        this(UserNotification.class, forVariable(variable), INITS);
    }

    public QUserNotification(Path<? extends UserNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserNotification(PathMetadata metadata, PathInits inits) {
        this(UserNotification.class, metadata, inits);
    }

    public QUserNotification(Class<? extends UserNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.sparta.gymspartaprojectbackend.user.entity.QUser(forProperty("user")) : null;
    }

}

