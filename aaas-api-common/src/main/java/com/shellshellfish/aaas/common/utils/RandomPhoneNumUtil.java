package com.shellshellfish.aaas.common.utils;

/**
 * @Author pierre
 * 18-1-6
 * generate random phone number
 */
public class RandomPhoneNumUtil {


	public static String generatePhoneNumber() {
		String[] telHead = {"134", "135", "136", "137", "138", "139", "150", "151", "152", "157", "158", "159", "130", "131", "132", "155", "156", "133", "153"};
		int index = getNum(0, telHead.length - 1);
		String first = telHead[index];
		String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
		String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
		return first + second + third;
	}

	public static int getNum(int start, int end) {
		return (int) (Math.random() * (end - start + 1) + start);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {

			System.out.println(generatePhoneNumber());
		}
	}
}
