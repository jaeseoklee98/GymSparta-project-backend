package com.sparta.gymspartaprojectbackend.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = -1927455802L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStore store = new QStore("store");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final ListPath<String, StringPath> memberships = this.<String, StringPath>createList("memberships", String.class, StringPath.class, PathInits.DIRECT2);

    public final com.sparta.gymspartaprojectbackend.owner.entity.QOwner owner;

    public final StringPath price = createString("price");

    public final ListPath<String, StringPath> ptConsultations = this.<String, StringPath>createList("ptConsultations", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> reviews = this.<String, StringPath>createList("reviews", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> services = this.<String, StringPath>createList("services", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath storeHour = createString("storeHour");

    public final StringPath storeInfo = createString("storeInfo");

    public final StringPath storeName = createString("storeName");

    public final StringPath storeTel = createString("storeTel");

    public final ListPath<String, StringPath> trainerList = this.<String, StringPath>createList("trainerList", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStore(String variable) {
        this(Store.class, forVariable(variable), INITS);
    }

    public QStore(Path<? extends Store> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStore(PathMetadata metadata, PathInits inits) {
        this(Store.class, metadata, inits);
    }

    public QStore(Class<? extends Store> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new com.sparta.gymspartaprojectbackend.owner.entity.QOwner(forProperty("owner")) : null;
    }

}

