package com.ieval.mockTool.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasedController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected int weightedRandom(int range, int biasedValue, int weight) {
	    List<Integer> numbers = new ArrayList<>();
	    for (int i = 0; i < weight; i++) {
	        numbers.add(biasedValue);
	    }
	    for (int i = 0; i < range; i++) {
	        if (i != biasedValue) {
	            numbers.add(i);
	        }
	    }
	    return numbers.get(ThreadLocalRandom.current().nextInt(numbers.size()));
	}
	
	protected int getRandomResult(double threshold) {
		double randomValue = Math.random();
		if (randomValue<=threshold) {
			return 0;
		}else {
			return 1;
		}
	}
	
	protected String generateRandomString(int len) {
		String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            int index = random.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }

        return result.toString();
    }
	
	protected String generateRandomNum(int len) {
		String CHARACTERS = "0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            int index = random.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }

        return result.toString();
    }
}

