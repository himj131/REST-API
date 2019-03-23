package me.star.springrestapi.common;

import me.star.springrestapi.index.IndexController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ErrorsResource extends Resource<Errors> {

    public ErrorsResource(Errors content, Link... links) {
        super(content, links);
        //에러를 받아서 아래 링크 추가.(IndexController 클래스에 있는 index라는 메소드로 가는 링크를 'index'라는 링크로 추가)
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}
