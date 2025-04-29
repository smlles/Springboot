package com.example.demo.di5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Car {
	@Autowired
	Engine engine;
	@Autowired
	Wheel wheel;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Car [engine=" + engine + ", wheel=" + wheel + "]";
	}
}
