package com.example.demo.di1;

import java.io.FileReader;
import java.util.Properties;

import ch.qos.logback.core.pattern.parser.FormattingNode;

//상속,클래스 타입변환, 오버라이딩은 기본으로 알아야함
class Car{};
class SportCar extends Car{};
class Truck extends Car{};
class Engine {};



public class Main1 {
	public static void main(String[] args)throws Exception {
		//변경 사항이 생겼다면, 타입과 생성자를 모두 바꿔야한다.
//		SportCar car = new SportCar() ;
//		Truck car = new Truck();
		
		//타입 변환을 이용하면, 수정 할 부분이 적어진다.
//		Car car = new SportCar();
//		Car car = new Truck();
		
		//별도의 메서드를 만들어서 객체를 생성하면 수정포인트를 더 줄일 수 있다.
		//사용하는 곳이 많아질수록 메서드로 객체를 생성하는 것이 유리
		Car car =(Car)getObject("sportcar");
		Car car2 =(Car)getObject("truck");
		Engine e = (Engine)getObject("engine");
		
		
		
		
		
		
		
	}
//	static Car getCar()throws Exception {
//		//txt파일을 읽어오기
//		//java.utill.Properties
//		//Key와 Value의 쌍으로 구성된 속성 목록을 관리 할 때 사용
//		Properties p = new Properties();
//		p.load(new FileReader("config.text"));
//		//텍스트 파일에서 car라는 key의 value를 찾아서 반환
//		Class clazz = Class.forName(p.getProperty("car"));
//		return (Car)clazz.newInstance();
//	}
	
	static Object getObject(String key) throws Exception{
		//텍스트 파일을 읽어서
		Properties p = new Properties();
		p.load(new FileReader("config.txt"));
		
		//인자로 받은 문자열을 가진 Key를 통해 Value를 가져온다.
		//value는 클래스 경로가 들어있다.
		Class clazz = Class.forName(p.getProperty(key));
		//clazz에 담겨있는 경로를 통해 NewInstance()로 객체 생성해서 반환
		return clazz.newInstance();
	}
	
}
