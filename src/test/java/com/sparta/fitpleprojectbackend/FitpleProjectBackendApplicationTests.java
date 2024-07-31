package com.sparta.fitpleprojectbackend;

import com.sparta.fitpleprojectbackend.ptsesson.service.PtPaymentService;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FitpleProjectBackendApplicationTests {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PtPaymentService ptPaymentService;


}
