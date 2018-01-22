package com.lyp.test.fsm;

import java.util.Random;

import com.lyp.uge.ai.fsm.State;

public class WanderState extends State {
	
	int wanderDir;

	@Override
	public void update(Object object) {
		if (((Enemy) object).findPlayer) {
			((Enemy) object).fsm.setCurrState(Enemy.ENEMY_STATE.alert.value);
			return;
		}
		
		((Enemy) object).saySomething = "Enemy: 巡逻中";
		
		wanderDir = new Random().nextInt(360);
		
		System.out.println(((Enemy) object).saySomething + ", 方向" + wanderDir);
	}

}
