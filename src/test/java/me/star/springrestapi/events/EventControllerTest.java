package me.star.springrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.star.springrestapi.commom.RestdocConfiguration;
import me.star.springrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)

//silcing test. 가짜 디스패쳐서블릿을 만들고 요청, 웹용 테스트라 repository빈은 주입을 안해준다.
// @WebMvcTest

//mocking할게 많을때는 slicing test가 아닌 boottest사용. 이때 @AutoConfigureMockMvc도 함께 쓴다.
@SpringBootTest
@AutoConfigureMockMvc
//rest docs
@AutoConfigureRestDocs
//다른스프링 bean 설정파일 읽어오기
@Import(RestdocConfiguration.class)
//원 application.properties를 사용하면서 application-test.properties도 사용하겠다.(공통 내용은 application.properties)
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean //Mockito를 사용해서 mock객체를 만들고 빈으로 등록해줌. 기존 빈을 테스트용빈으로 등록한다.
//    EventRepository eventRepository

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        //주어진 요청 만들기
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,03,22,15, 32))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,03,23,15, 50))
                .beginEventDateTime(LocalDateTime.of(2019,04, 03, 11, 00))
                .endEventDateTime(LocalDateTime.of(2019, 04,04,11,00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 팩토리")
                .build();

        //repository를 목빈으로 등록했기 때문에 save등 뭘하든 null이 반환된다.
        //이럴경우 테스트를 save후 event를 리턴해라 라는 행위?추가. 반환할때 eventid 필요하므로 세팅해줌.
//        event.setId(10);
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        //테스트할 동작
        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())//201=created 응답 isCreated() = is(201)
                .andExpect(jsonPath("id").exists())  //id값이 나오는지 확인
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                //HATEOAS
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                //rest docs
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to update an existing event")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")

                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        //relaxed 라는 prefix 사용시 모든 필드를 문서화 시키지 않는다.
                        relaxedResponseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("it thells is this event is free or not"),
                                fieldWithPath("offline").description("it thells is this event is offline or not"),
                                fieldWithPath("eventStatus").description("event status")
                        )
               ));
    }

    @Test
    @TestDescription("입력 받을수 없는 값을 사용한 경우 에러 발생 테스트")
    public void createEvent_Bad_Reqeust() throws Exception {

        //주어진 요청 만들기
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,03,22,15, 32))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,03,23,15, 50))
                .beginEventDateTime(LocalDateTime.of(2019,04, 03, 11, 00))
                .endEventDateTime(LocalDateTime.of(2019, 04,04,11,00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 팩토리")
                .free(true)
                .offline(false)
                .build();


        //테스트할 동작
        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())

        ;
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생 하는 테스트")
    public void createEvent_Bad_Reqeust_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }


    //max가 base값보다 작을때, 끝날이 시작날보다 이를때 등.
    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생 하는 테스트")
    public void createEvent_Bad_Reqeust_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,03,22,15, 32))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,03,21,15, 50))
                .beginEventDateTime(LocalDateTime.of(2019,04, 03, 11, 00))
                .endEventDateTime(LocalDateTime.of(2019, 04,03,10,00))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
        ;
    }
}
