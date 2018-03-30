package com.lyp.uge.ai.sorting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.lyp.uge.particle.Particle;

public class InsertionSort {

	/**
	 * Using Java reflect.
	 * 
	 * @param list
	 * @param methodName
	 */
	public static <T> void SortHighToLow(List<T> list, String methodName) {
		int i, j;
		for (i = 1; i < list.size(); i++) {
			T object = list.get(i);
			try {
				Method method = object.getClass().getMethod(methodName, null);
				float val = (float) method.invoke(object, null);
				j = i - 1;
				T pre = list.get(j);
				while (j >= 0 && val > (float) method.invoke(pre, null)) {
					T temp = list.get(j);
					list.set(j, list.get(j + 1));
					list.set(j + 1, temp);
					j--;
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * For particles data.
	 * 
	 * @param list
	 */
	public static void sortHighToLow(List<Particle> list) {
		for (int i = 1; i < list.size(); i++) {
			Particle item = list.get(i);
			if (item.getDistanceFromCamera() > list.get(i - 1).getDistanceFromCamera()) {
				sortUpHighToLow(list, i);
			}
		}
	}

	private static void sortUpHighToLow(List<Particle> list, int i) {
		Particle item = list.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).getDistanceFromCamera() < item.getDistanceFromCamera()) {
			attemptPos--;
		}
		list.remove(i);
		list.add(attemptPos, item);
	}
}
