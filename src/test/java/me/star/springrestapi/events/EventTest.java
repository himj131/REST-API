package me.star.springrestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with spring")
                .build();

        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){

        //Given
        String name="Event";
        String description = "Spring";

        //When
        Event event = new Event();
        event.setName("Event");
        event.setDescription("Spring");

        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    @Parameters(method = "parametersForTestFree") //메소드 방법 사용 안할시 Parameters괄호 안에 직접 값을 배열로 입력가능 'parametersFor테스트명' 형태의 메소드 명일경우 이부분 생략 가능
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);


//        //given
//        Event event = Event.builder()
//                .basePrice(0)
//                .maxPrice(0)
//                .build();
//
//        //when
//        event.update();
//
//        //then
//        assertThat(event.isFree()).isTrue();
//
//        //given
//        event = Event.builder()
//                .basePrice(100)
//                .maxPrice(0)
//                .build();
//
//        //when
//        event.update();
//
//        //then
//        assertThat(event.isFree()).isFalse();
//
//        //given
//        event = Event.builder()
//                .basePrice(0)
//                .maxPrice(1000)
//                .build();
//
//        //when
//        event.update();
//
//        //then
//        assertThat(event.isFree()).isFalse();
    }

    private Object[] parametersForTestFree(){
        return new Object[]{
                new Object[] { 0,0,true},
                new Object[] { 100,0,false},
                new Object[] { 0,100,false},
                new Object[] { 100,200,false}
        };
    }

    @Test
    @Parameters
    public void  testOffline(String location, boolean isOffline){
        //given
        Event event = Event.builder()
                .location(location)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);

//        //given
//        Event event = Event.builder()
//                .location("강남역 스타텁")
//                .build();
//
//        //when
//        event.update();
//
//        //then
//        assertThat(event.isFree()).isTrue();
//
//        //given
//        event = event.builder()
//                .build();
//
//        //when
//        event.update();
//
//        //then
//        assertThat(event.isFree()).isTrue();
    }

    private Object[] parametersForTestOffline(){
        return new Object[]{
                new Object[] { "강남", true},
                new Object[] { null, false},
                new Object[] { "  ", false}
        };
    }

}