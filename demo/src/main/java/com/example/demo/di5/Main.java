package com.example.demo.di5;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		// 스프링부트에서 제공하는 객체 컨테이너 클래스가 있다.
		//내부에서 Map으로 클래스로 객체를 value로 저장하기 때문에 객체 컨테이너라고 한다.
		
		// "com.example.demo.di5" 패키지 하위의 클래스중 어노테이션이 있는 것들을 메모리에 올려줘
		ApplicationContext ac = 
				//우리가 앞에 만든 AppContext의 역할을 한다.
				new AnnotationConfigApplicationContext("com.example.demo.di5");

		Car car = (Car) ac.getBean(Car.class);
//		Engine engine = (Engine) ac.getBean(Engine.class);
//		Wheel wheel = (Wheel) ac.getBean(Wheel.class);

		System.out.println(car);

	}
}
