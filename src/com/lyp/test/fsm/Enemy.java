package com.lyp.test.fsm;

import java.util.Random;

import com.lyp.uge.ai.fsm.StateMachine;

public class Enemy {

	public String saySomething = "我是一个小兵";
	
	public StateMachine fsm;
	
	protected boolean findPlayer = false;
	
	public Enemy() {
		this.fsm = new StateMachine();
		this.fsm.addState(ENEMY_STATE.idle.value, new IdleState())
				.addState(ENEMY_STATE.wander.value, new WanderState())
				.addState(ENEMY_STATE.alert.value, new AlertState())
				.addState(ENEMY_STATE.attack.value, new AttackState())
				.setCurrState(ENEMY_STATE.idle.value);
	}
	
	public void update() {
		int c = new Random().nextInt(10);
		if (c % 5 == 0) { //模拟碰撞检测
			System.out.println("（与Player发生碰撞" + c + "）");
			findPlayer = true;
		}
		
		fsm.update(this);
	}
	
	public static enum ENEMY_STATE {
		idle(0),
		wander(1),
		alert(2),
		attack(3);
		
		int value;
		ENEMY_STATE(int value) {
			this.value = value;
		}
	}
	
	public static void main(String[] args) {
		Enemy enemy = new Enemy();
		for (int i = 0; i < 100; i++) {
			enemy.update();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
