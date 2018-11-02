package com.test;

import java.io.File;

public class Test {
	public static void main(String[] args) {
		String filePath = "C:\\Users\\lenovo\\Desktop\\test";
		File file = new File(filePath);
		File[] files = file.listFiles();
		for (File f : files){
			System.out.println(f.getName());
		}
	}

}
