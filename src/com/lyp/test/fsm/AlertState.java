package com.lyp.test.fsm;

import com.lyp.uge.ai.fsm.State;

public class AlertState extends State {
	
	int timer = 0;

	@Override
	public void update(Object object) {
		((Enemy) object).saySomething = "Enemy: 发现玩家";
		System.out.println(((Enemy) object).saySomething);
		timer++;
		if (timer >= 3) {
			timer = 0;
			((Enemy) object).fsm.setCurrState(Enemy.ENEMY_STATE.attack.value);
		}
	}

}
