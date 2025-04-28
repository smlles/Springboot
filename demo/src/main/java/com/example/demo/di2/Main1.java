package com.example.demo.di2;

import java.util.HashMap;
import java.util.Map;

class Car{};
class SportCar extends Car{};
class Truck extends Car{};
class Engine{};

//객체 컨테이너로 객체 저장
//객체 컨테이너 : 객체들을 저장하는 공간
//class 안에 Map으로 객체를 저장

class AppContext{
	Map map; //객체 저장소
	public AppContext() {
		//객체를 메모리에 올리고 시작
		map = new HashMap();
		map.put("car", new SportCar());
		map.put("engine",new Engine());
	}
	//Key로 반환
	Object getBean(String key) {
		return map.get(key);
	}
}





public class Main1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AppContext ac = new AppContext();
		
		//필요할 때마다 호출해서 사용
		Car car = (Car)ac.getBean("car");
		System.out.println("car= "+car);
		
		Engine engine = (Engine)ac.getBean("engine");
		System.out.println("Engine = "+engine);
		
	}

}
