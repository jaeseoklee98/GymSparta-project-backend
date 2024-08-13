package com.sparta.fitpleprojectbackend.dataLoder;

import com.sparta.fitpleprojectbackend.enums.Role;
import com.sparta.fitpleprojectbackend.owner.entity.Owner;
import com.sparta.fitpleprojectbackend.owner.repository.OwnerRepository;
import com.sparta.fitpleprojectbackend.payment.entity.Payment;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentType;
import com.sparta.fitpleprojectbackend.payment.enums.PtTimes;
import com.sparta.fitpleprojectbackend.payment.repository.PaymentRepository;
import com.sparta.fitpleprojectbackend.product.entity.Product;
import com.sparta.fitpleprojectbackend.product.repository.ProductRepository;
import com.sparta.fitpleprojectbackend.review.entity.Review;
import com.sparta.fitpleprojectbackend.review.enums.ReviewType;
import com.sparta.fitpleprojectbackend.review.repository.ReviewRepository;
import com.sparta.fitpleprojectbackend.store.entity.Store;
import com.sparta.fitpleprojectbackend.store.repository.StoreRepository;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final TrainerRepository trainerRepository;
  private final OwnerRepository ownerRepository;
  private final PasswordEncoder passwordEncoder;
  private final ProductRepository productRepository;
  private final ReviewRepository reviewRepository;
  private final PaymentRepository paymentRepository;


  public DataLoader(UserRepository userRepository, StoreRepository storeRepository, PasswordEncoder passwordEncoder, OwnerRepository ownerRepository
  , TrainerRepository trainerRepository, ProductRepository productRepository,
    ReviewRepository reviewRepository, PaymentRepository paymentRepository) {
    this.userRepository = userRepository;
    this.trainerRepository = trainerRepository;
    this.storeRepository = storeRepository;
    this.ownerRepository = ownerRepository;
    this.passwordEncoder = passwordEncoder;
    this.productRepository = productRepository;
    this.reviewRepository = reviewRepository;
    this.paymentRepository = paymentRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    // 유저 더미 데이터
    User user1 = new User(
      "손아엘",                      // userName
      1000.0,                           // balance
      "111111222222",                 // residentRegistrationNumber
      null,                             // foreignerRegistrationNumber
      false,                            // isForeigner
      "qqq111111",                       // accountId
      passwordEncoder.encode("password123"),   // password
      "손아엘닉네임",                         // nickname
      "john@example.com",               // email
      null,                             // userPicture
      "ACTIVE",                         // status
      "12345",                          // zipcode
      "서울시",                    // mainAddress
      "강남구",                         // detailedAddress
      "010-1234-5676",                  // phoneNumber
      Role.USER,                        // role
      null,                             // deletedAt
      null                              // scheduledDeletionDate
    );

    User user2 = new User(
      "이인빈",                      // userName
      1000.0,                           // balance
      "333333444444",                 // residentRegistrationNumber
      null,                             // foreignerRegistrationNumber
      false,                            // isForeigner
      "qqq222222",                       // accountId
      passwordEncoder.encode("password123"),   // password
      "이인빈닉네임",                         // nickname
      "qqq111@example.com",               // email
      null,                             // userPicture
      "ACTIVE",                         // status
      "12345",                          // zipcode
      "울산시",                    // mainAddress
      "울산구",                         // detailedAddress
      "010-1234-5677",                  // phoneNumber
      Role.USER,                        // role
      null,                             // deletedAt
      null                              // scheduledDeletionDate
    );

    User user3 = new User(
      "이여재",                      // userName
      1000.0,                           // balance
      "555555666666",                 // residentRegistrationNumber
      null,                             // foreignerRegistrationNumber
      false,                            // isForeigner
      "qqq333333",                       // accountId
      passwordEncoder.encode("password123"),   // password
      "이여재닉네임",                         // nickname
      "qqq222@example.com",               // email
      null,                             // userPicture
      "ACTIVE",                         // status
      "12345",                          // zipcode
      "대구시",                    // mainAddress
      "대구구",                         // detailedAddress
      "010-1234-5678",                  // phoneNumber
      Role.USER,                        // role
      null,                             // deletedAt
      null                              // scheduledDeletionDate
    );

    userRepository.save(user1);
    userRepository.save(user2);
    userRepository.save(user3);
    
    System.out.println("유저 더미 데이터 삽입 완료!");

    // 더미 데이터 생성
    Owner owner1 = new Owner(
      "손아엘점주",
      "1234561234567",
      null,  // 외국인등록번호 (해당사항 없음)
      false, // 한국인
      "www111",
      passwordEncoder.encode("password123"),
      "손아엘점주닉네임",
      "johndoe@example.com",
      "john_picture.jpg",
      "ACTIVE",
      "1234567890",
      "손아엘점주비즈니스닉네임",
      "12345",
      "서울",
      "강남",
      "010-1234-5678",
      Role.OWNER,
      null,
      null
    );

    Owner owner2 = new Owner(
      "이여재점주",
      "1234561234577",
      null,
      false,
      "www222",
      passwordEncoder.encode("password123"),
      "이여재점주닉네임",
      "janesmith@example.com",
      "jane_picture.jpg",
      "ACTIVE",
      "9876543210",
      "이여재점주비즈니스닉네임",
      "54321",
      "데구",
      "데구리",
      "010-8765-4321",
      Role.OWNER,
      null,
      null
    );

    Owner owner3 = new Owner(
      "이재석점주",
      "1234561234587",
      null,
      false,
      "www333",
      passwordEncoder.encode("password123"),
      "이재석점주닉네임",
      "janesmith@example.com",
      "jane_picture.jpg",
      "ACTIVE",
      "9876543210",
      "이재석점주비즈니스닉네임",
      "54321",
      "창원",
      "창원리",
      "010-8765-4321",
      Role.OWNER,
      null,
      null
    );

    ownerRepository.save(owner1);
    ownerRepository.save(owner2);
    ownerRepository.save(owner3);

    System.out.println("오너 더미 데이터 삽입 완료!");
    
    // 매장 더미 데이터
    Store store1 = new Store(
      "아에리가게",
      "강남",
      "아에리의메이플가게",
      "9 AM - 9 PM",
      "123-456-7890",
      "$50 - $200",
      "fitness_hub.jpg",
      owner1
    );

    Store store2 = new Store(
      "이여재가게",
      "압구정",
      "이여재의서든가게",
      "6 AM - 10 PM",
      "987-654-3210",
      "$5 - $200",
      "fitness_hubb.jpg",
      owner2
    );

    Store store3 = new Store("이재석가개",
      "홍대",
      "이재석의코딩가게",
      "6 AM - 10 PM",
      "987-654-3210",
      "$9000 - $10000",
      "fitness_hubb.jpg",
      owner3
    );

    storeRepository.save(store1);
    storeRepository.save(store2);
    storeRepository.save(store3);

    System.out.println("매장 더미 데이터 삽입 완료!");

    // 더미 데이터 생성
    Trainer trainer1 = new Trainer(
      "손아엘트레이너",
      50.0,
      "손아엘메이플7랩",
      "ttt111",
      passwordEncoder.encode("password123"),
      "손아엘트레이너닉네임",
      "johndoe@example.com",
      "john_doe.jpg",
      "ACTIVE",
      "010-1234-5678",
      Role.TRAINER,
      null,  // 삭제일이 필요 없는 경우 null
      store1
    );

    Trainer trainer2 = new Trainer(
      "이여재트레이너",
      60.0,
      "이여재서든초고수",
      "ttt222",
      passwordEncoder.encode("password123"),
      "이여재트레이너닉네임",
      "janesmith@example.com",
      "jane_smith.jpg",
      "ACTIVE",
      "010-8765-4321",
      Role.TRAINER,
      null,  // 삭제일이 필요 없는 경우 null
      store2
    );

    Trainer trainer3 = new Trainer(
      "이재석트레이너",
      55.0,
      "이재석코딩초고수",
      "ttt333",
      "password789",
      "이재석트레이너닉네임",
      "emilyjohnson@example.com",
      "emily_johnson.jpg",
      "ACTIVE",
      "010-1357-2468",
      Role.TRAINER,
      null,// 삭제일이 필요 없는 경우 null
      store3
    );
    
    trainerRepository.save(trainer1);
    trainerRepository.save(trainer2);
    trainerRepository.save(trainer3);

    System.out.println("트레이너 더미 데이터 삽입 완료!");

    // 더미 데이터 생성
    Product product1 = new Product(
      trainer1,
      user1,
      PtTimes.TEN_TIMES,
      PaymentType.CREDIT_CARD,
      500.0,
      450.0,
      PaymentStatus.COMPLETED,
      LocalDateTime.now().minusDays(10),
      LocalDateTime.now().plusMonths(1),
      true
    );

    Product product2 = new Product(
      trainer2,
      user2,
      PtTimes.TWENTY_TIMES,
      PaymentType.UNDEFINED,
      300.0,
      270.0,
      PaymentStatus.PENDING,
      LocalDateTime.now().minusDays(5),
      LocalDateTime.now().plusWeeks(2),
      true
    );

    Product product3 = new Product(
      trainer3,
      user3,
      PtTimes.THIRTY_TIMES,
      PaymentType.CREDIT_CARD,
      100.0,
      90.0,
      PaymentStatus.FAILED,
      LocalDateTime.now().minusDays(1),
      LocalDateTime.now().plusDays(7),
      false
    );
    
    productRepository.save(product1);
    productRepository.save(product2);
    productRepository.save(product3);
    System.out.println("상품 더미 데이터 삽입 완료!");

    // 더미 Payment 데이터 생성
    Payment payment1 = new Payment(trainer1, user1, store1, product1, PtTimes.TEN_TIMES, PaymentType.CREDIT_CARD, 1200.0, PaymentStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now().plusMonths(1), true);

    paymentRepository.save(payment1);

    System.out.println("결제 더미 데이터 삽입 완료!");


    // 더미 리뷰 데이터 생성
    Review review1 = new Review(user1, store1, null, payment1, null, 5, "Great expe23rience!", ReviewType.STORE);
    Review review2 = new Review(user1, store1, null, payment1, null, 5, "Great exp1erience!", ReviewType.STORE);
    Review review3 = new Review(user1, store1, null, payment1, null, 5, "Great exㄴperience!", ReviewType.STORE);
    Review review4 = new Review(user1, null, trainer1, payment1, product1, 4, "Trainer was very professional.", ReviewType.TRAINER);
    Review review5 = new Review(user1, null, trainer1, payment1, product1, 4, "Trainer wasㄴ very professional.", ReviewType.TRAINER);
    Review review6 = new Review(user1, null, trainer1, payment1, product1, 4, "Trainer was very professional.", ReviewType.TRAINER);
    Review review7 = new Review(user1, store1, trainer1, payment1, product1, 3, "Average servi3ce, but the trai5ner was good.", ReviewType.STORE);
    Review review8 = new Review(user1, store1, trainer1, payment1, product1, 3, "Aㅇverage service, but the train56er was good.", ReviewType.STORE);
    Review review9 = new Review(user1, store1, trainer1, payment1, product1, 3, "Avㅇerage ser45vice, but the trainer was good.", ReviewType.STORE);
    
    reviewRepository.save(review1);
    reviewRepository.save(review2);
    reviewRepository.save(review3);
    reviewRepository.save(review4);
    reviewRepository.save(review5);
    reviewRepository.save(review6);
    reviewRepository.save(review7);
    reviewRepository.save(review8);
    reviewRepository.save(review9);

    System.out.println("리뷰 더미 데이터 삽입 완료!");
  }
}
