package com.sparta.gymspartaprojectbackend.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 1765387248L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.sparta.gymspartaprojectbackend.common.QTimeStamped _super = new com.sparta.gymspartaprojectbackend.common.QTimeStamped(this);

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.sparta.gymspartaprojectbackend.payment.entity.QPayment payment;

    public final com.sparta.gymspartaprojectbackend.product.entity.QProduct product;

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final ListPath<ReviewImage, QReviewImage> reviewImages = this.<ReviewImage, QReviewImage>createList("reviewImages", ReviewImage.class, QReviewImage.class, PathInits.DIRECT2);

    public final EnumPath<com.sparta.gymspartaprojectbackend.review.enums.ReviewType> reviewType = createEnum("reviewType", com.sparta.gymspartaprojectbackend.review.enums.ReviewType.class);

    public final com.sparta.gymspartaprojectbackend.store.entity.QStore store;

    public final com.sparta.gymspartaprojectbackend.trainer.entity.QTrainer trainer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.gymspartaprojectbackend.user.entity.QUser user;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.payment = inits.isInitialized("payment") ? new com.sparta.gymspartaprojectbackend.payment.entity.QPayment(forProperty("payment"), inits.get("payment")) : null;
        this.product = inits.isInitialized("product") ? new com.sparta.gymspartaprojectbackend.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
        this.store = inits.isInitialized("store") ? new com.sparta.gymspartaprojectbackend.store.entity.QStore(forProperty("store"), inits.get("store")) : null;
        this.trainer = inits.isInitialized("trainer") ? new com.sparta.gymspartaprojectbackend.trainer.entity.QTrainer(forProperty("trainer"), inits.get("trainer")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.gymspartaprojectbackend.user.entity.QUser(forProperty("user")) : null;
    }

}

