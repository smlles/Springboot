package com.example.dependency.qualifier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("desktop")
public class Desktop implements Computer {
	@Override
	public int getScreenWidth() {
		// TODO Auto-generated method stub
		return 1440;
	}
}
