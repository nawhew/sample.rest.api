package com.example.sample.rest.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
/*
 * @SpringBootTest의 webEnvironment값이 SpringBootTest.WebEnvironment.MOCK)가 default이기 때문에
 * 설정을 안해도 Mock을 계속 사용 가능
 * 대신 아래와 같이 AutoConfigure 어노테이션을 달아 주어야 한다.
 * */
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@Ignore // Test를 가지고 있지 않은 클래스임을 알려줌
public class BaseContorllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;
}
