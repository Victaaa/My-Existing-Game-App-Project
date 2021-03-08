package com.game.maths.Models;

public class DataVars {

	public int one;
	public boolean isAdd;
    public int operator;
	public boolean isCorrect;
	public int resources;
	public int two;

	public DataVars(int one, int two, int resources, boolean isCorrect,
			boolean isAdd, int operator) {
		this.one = one;
		this.two = two;
		this.resources = resources;
		this.isCorrect = isCorrect;
		this.isAdd = isAdd;
        this.operator = operator;
	}
}
