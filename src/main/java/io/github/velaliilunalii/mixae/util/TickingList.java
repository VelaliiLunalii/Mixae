package io.github.velaliilunalii.mixae.util;

import java.util.ArrayList;
import java.util.List;

public class TickingList<T extends IEffect> {
	private ArrayList<T> firstList;
	private ArrayList<T> secondList;
	private final int cooldown;
	private int firstListEffectCountdown;
	private int secondListEffectCountdown;

	public TickingList(){
		this.cooldown = 10;
		this.firstListEffectCountdown = this.cooldown;
		this.secondListEffectCountdown = this.cooldown;
		this.firstList = new ArrayList<>();
		this.secondList = new ArrayList<>();
	}

	public TickingList(int cooldown){
		this.cooldown = cooldown;
		this.firstListEffectCountdown = this.cooldown;
		this.secondListEffectCountdown = this.cooldown;
		this.firstList = new ArrayList<>();
		this.secondList = new ArrayList<>();
	}

	public TickingList(List<T> list){
		this.cooldown = 10;
		this.firstListEffectCountdown = this.cooldown;
		this.secondListEffectCountdown = this.cooldown;
		this.firstList = new ArrayList<>(list);
		this.secondList = new ArrayList<>();
	}

	public TickingList(int cooldown, List<T> list){
		this.cooldown = cooldown;
		this.firstListEffectCountdown = this.cooldown;
		this.secondListEffectCountdown = this.cooldown;
		this.firstList = new ArrayList<>(list);
		this.secondList = new ArrayList<>();
	}

	public void tick(){
		if (this.firstListEffectCountdown <= 0){
			this.firstListEffectCountdown = this.cooldown;
			this.firstListEffect();
		}else{
			this.firstListEffectCountdown--;
		}
		if (this.secondListEffectCountdown <= 0){
			this.secondListEffectCountdown = this.cooldown;
			this.secondListEffect();
		}else if(!secondList.isEmpty()){
			this.secondListEffectCountdown--;
		}
	}

	private void firstListEffect(){
		T element = firstList.remove(0);
		secondList.add(element);
		element.effect();
		element.toggleUsedOnce();
	}

	private void secondListEffect(){
		T element = secondList.remove(0);
		element.effect();
	}

	public Boolean isEmpty(){
		return firstList.isEmpty() && secondList.isEmpty();
	}
}
