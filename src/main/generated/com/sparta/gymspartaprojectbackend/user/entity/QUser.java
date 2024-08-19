package com.sparta.gymspartaprojectbackend.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 27304726L;

    public static final QUser user = new QUser("user");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    public final StringPath accountId = createString("accountId");

    public final NumberPath<Double> balance = createNumber("balance", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath detailedAddress = createString("detailedAddress");

    public final StringPath email = createString("email");

    public final StringPath foreignerRegistrationNumber = createString("foreignerRegistrationNumber");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isForeigner = createBoolean("isForeigner");

    public final StringPath mainAddress = createString("mainAddress");

    public final DateTimePath<java.time.LocalDateTime> membershipExpiryDate = createDateTime("membershipExpiryDate", java.time.LocalDateTime.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath residentRegistrationNumber = createString("residentRegistrationNumber");

    public final EnumPath<com.sparta.gymspartaprojectbackend.enums.Role> role = createEnum("role", com.sparta.gymspartaprojectbackend.enums.Role.class);

    public final DateTimePath<java.time.LocalDateTime> scheduledDeletionDate = createDateTime("scheduledDeletionDate", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<com.sparta.gymspartaprojectbackend.notification.entity.UserAllNotification, com.sparta.gymspartaprojectbackend.notification.entity.QUserAllNotification> userAllNotificationList = this.<com.sparta.gymspartaprojectbackend.notification.entity.UserAllNotification, com.sparta.gymspartaprojectbackend.notification.entity.QUserAllNotification>createList("userAllNotificationList", com.sparta.gymspartaprojectbackend.notification.entity.UserAllNotification.class, com.sparta.gymspartaprojectbackend.notification.entity.QUserAllNotification.class, PathInits.DIRECT2);

    public final StringPath userName = createString("userName");

    public final ListPath<com.sparta.gymspartaprojectbackend.notification.entity.UserNotification, com.sparta.gymspartaprojectbackend.notification.entity.QUserNotification> userNotificationList = this.<com.sparta.gymspartaprojectbackend.notification.entity.UserNotification, com.sparta.gymspartaprojectbackend.notification.entity.QUserNotification>createList("userNotificationList", com.sparta.gymspartaprojectbackend.notification.entity.UserNotification.class, com.sparta.gymspartaprojectbackend.notification.entity.QUserNotification.class, PathInits.DIRECT2);

    public final StringPath userPicture = createString("userPicture");

    public final StringPath zipcode = createString("zipcode");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

