package me.star.springrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest //silcing test. 가짜 디스패쳐서블릿을 만들고 요청, 웹용 테스트라 repository빈은 주입을 안해준다.
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean //Mockito를 사용해서 mock객체를 만들고 빈으로 등록해줌. 기존 빈을 테스트용빈으로 등록한다.
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {

        //주어진 요청 만들기
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEventDateTime(LocalDateTime.of(2019,03,22,15, 32))
                .endEventDateTime(LocalDateTime.of(2019,03,23,15, 50))
                .beginEventDateTime(LocalDateTime.of(2019,04, 03, 11, 00))
                .endEventDateTime(LocalDateTime.of(2019, 04,04,11,00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 팩토리")
                .build();

        //repository를 목빈으로 등록했기 때문에 save등 뭘하든 null이 반환된다.
        //이럴경우 테스트를 save후 event를 리턴해라 라는 행위?추가. 반환할때 eventid 필요하므로 세팅해줌.
        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        //테스트할 동작
        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())//201=created 응답 isCreated() = is(201)
                .andExpect(jsonPath("id").exists())  //id값이 나오는지 확인
        ;
    }
}