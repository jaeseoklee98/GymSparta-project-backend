package com.sparta.gymspartaprojectbackend.owner.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOwner is a Querydsl query type for Owner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOwner extends EntityPathBase<Owner> {

    private static final long serialVersionUID = 1932091654L;

    public static final QOwner owner = new QOwner("owner");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    public final StringPath accountId = createString("accountId");

    public final StringPath businessName = createString("businessName");

    public final StringPath businessRegistrationNumber = createString("businessRegistrationNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath detailedAddress = createString("detailedAddress");

    public final StringPath email = createString("email");

    public final StringPath foreignerRegistrationNumber = createString("foreignerRegistrationNumber");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isForeigner = createBoolean("isForeigner");

    public final StringPath mainAddress = createString("mainAddress");

    public final StringPath nickname = createString("nickname");

    public final StringPath ownerName = createString("ownerName");

    public final StringPath ownerPhoneNumber = createString("ownerPhoneNumber");

    public final StringPath ownerPicture = createString("ownerPicture");

    public final StringPath ownerStatus = createString("ownerStatus");

    public final StringPath password = createString("password");

    public final StringPath residentRegistrationNumber = createString("residentRegistrationNumber");

    public final EnumPath<com.sparta.gymspartaprojectbackend.enums.Role> role = createEnum("role", com.sparta.gymspartaprojectbackend.enums.Role.class);

    public final DateTimePath<java.time.LocalDateTime> scheduledDeletionDate = createDateTime("scheduledDeletionDate", java.time.LocalDateTime.class);

    public final ListPath<com.sparta.gymspartaprojectbackend.store.entity.Store, com.sparta.gymspartaprojectbackend.store.entity.QStore> stores = this.<com.sparta.gymspartaprojectbackend.store.entity.Store, com.sparta.gymspartaprojectbackend.store.entity.QStore>createList("stores", com.sparta.gymspartaprojectbackend.store.entity.Store.class, com.sparta.gymspartaprojectbackend.store.entity.QStore.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath zipcode = createString("zipcode");

    public QOwner(String variable) {
        super(Owner.class, forVariable(variable));
    }

    public QOwner(Path<? extends Owner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOwner(PathMetadata metadata) {
        super(Owner.class, metadata);
    }

}

