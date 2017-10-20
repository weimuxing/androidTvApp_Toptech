package com.toptech.launcherkorea2.logic;

import java.util.HashMap;

/**
 * @author calvin
 *
 */
public class Task {
	
	private int taskID;
	private HashMap<String,Object> taskParam;

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public HashMap<String,Object> getTaskParam() {
		return taskParam;
	}

	public void setTaskParam(HashMap<String,Object> taskParam) {
		this.taskParam = taskParam;
	}

	public Task(int tsid, HashMap<String,Object> hm) {
		this.taskID = tsid;
		this.taskParam = hm;
	}
}
