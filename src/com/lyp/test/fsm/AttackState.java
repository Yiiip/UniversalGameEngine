package com.lyp.test.fsm;

import com.lyp.uge.ai.fsm.State;

public class AttackState extends State {

	@Override
	public void update(Object object) {
		((Enemy) object).saySomething = "Enemy: 攻击玩家";
		System.out.println(((Enemy) object).saySomething);
		
		((Enemy) object).fsm.setCurrState(Enemy.ENEMY_STATE.idle.value);
		((Enemy) object).findPlayer = false;
	}

}
