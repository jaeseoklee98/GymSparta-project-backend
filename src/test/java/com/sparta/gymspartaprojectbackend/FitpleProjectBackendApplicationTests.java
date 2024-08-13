package com.sparta.gymspartaprojectbackend;

import com.sparta.gymspartaprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.gymspartaprojectbackend.user.repository.UserRepository;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FitpleProjectBackendApplicationTests {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

//    @InjectMocks
//    private PtPaymentService ptPaymentService;


}
