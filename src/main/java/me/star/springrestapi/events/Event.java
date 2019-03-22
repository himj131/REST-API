package me.star.springrestapi.events;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
//롬복 애노테이션은 커스터마이징한 애노테이션을 만들어 애노테이션 수를 줄일수 없다.(아직까지는..)
@Builder @AllArgsConstructor @NoArgsConstructor //각각 빌더 생성, 모든 arg를 가지고 있는 생성자, 기본생성자 만들기 위함
@Getter @Setter @EqualsAndHashCode(of= {"id", "name"}) //EqualsAndHashCode 구현할때 모든 entity를 구현을 하는데 연관관계가 들어갈때 id기반으로 equeals체크 해라
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

}
