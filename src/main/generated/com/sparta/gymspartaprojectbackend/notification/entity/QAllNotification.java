package com.sparta.gymspartaprojectbackend.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAllNotification is a Querydsl query type for AllNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAllNotification extends EntityPathBase<AllNotification> {

    private static final long serialVersionUID = -608953631L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAllNotification allNotification = new QAllNotification("allNotification");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath message = createString("message");

    public final com.sparta.gymspartaprojectbackend.store.entity.QStore store;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<UserAllNotification, QUserAllNotification> userAllNotificationList = this.<UserAllNotification, QUserAllNotification>createList("userAllNotificationList", UserAllNotification.class, QUserAllNotification.class, PathInits.DIRECT2);

    public QAllNotification(String variable) {
        this(AllNotification.class, forVariable(variable), INITS);
    }

    public QAllNotification(Path<? extends AllNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAllNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAllNotification(PathMetadata metadata, PathInits inits) {
        this(AllNotification.class, metadata, inits);
    }

    public QAllNotification(Class<? extends AllNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new com.sparta.gymspartaprojectbackend.store.entity.QStore(forProperty("store"), inits.get("store")) : null;
    }

}

