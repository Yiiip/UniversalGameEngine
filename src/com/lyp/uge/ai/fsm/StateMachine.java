package com.lyp.uge.ai.fsm;

import java.util.ArrayList;
import java.util.List;

/**
 * Finite State Machine
 * @author LYP
 * 
 */
public class StateMachine {

	private List<State> mStateList;
	
	private State currState = null;
	private State prevState = null;
	
	public StateMachine() {
		this.mStateList = new ArrayList<State>();
	}
	
	public void update(Object object) {
		if (currState != null) {
			currState.update(object);
			prevState = currState;
		}
	}
	
	public StateMachine addState(int index, State state) {
		if (mStateList != null && !mStateList.contains(state)) {
			mStateList.add(index, state);
		}
		return this;
	}
	
	public StateMachine removeState(State state) {
		if (mStateList != null) {
			mStateList.remove(state);
		}
		return this;
	}
	
	public void setCurrState(int currIndex) {
		if (mStateList != null) {
			this.prevState = this.currState;
			this.currState = mStateList.get(currIndex);
		}
	}
	
	public State getCurrState() {
		return currState;
	}
	
	public State getPrevState() {
		return prevState;
	}
	
	public void clearAll() {
		if (mStateList != null) {
			mStateList.clear();
		}
		currState = null;
		prevState = null;
	}
}
