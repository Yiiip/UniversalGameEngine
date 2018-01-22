package com.lyp.test.fsm;

import com.lyp.uge.ai.fsm.State;

public class IdleState extends State {

	int timer = 0;

	@Override
	public void update(Object object) {
		if (((Enemy) object).findPlayer) {
			((Enemy) object).fsm.setCurrState(Enemy.ENEMY_STATE.alert.value);
			return;
		}
		
		((Enemy) object).saySomething = "Enemy: 闲置中";
		System.out.println(((Enemy) object).saySomething);
		timer++;
		if (timer >= 3) {
			timer = 0;
			((Enemy) object).fsm.setCurrState(Enemy.ENEMY_STATE.wander.value);
		}
	}

}
