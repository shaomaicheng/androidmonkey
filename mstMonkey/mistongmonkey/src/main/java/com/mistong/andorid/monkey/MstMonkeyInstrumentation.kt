package com.mistong.andorid.monkey

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.SparseArray

/**
 *  create by chenglei at 2018/8/17
 *  Application start this Instrumentation for APP Monkey
 */
class MstMonkeyInstrumentation : Instrumentation() {

    companion object {
        val TAG = "MonkeyInstrumentation"
    }

    private val checkTaskInterval = 5 * 1000L
    private val topActivitySurvivalTime = 10 * 1000L
    private val stackActivitySurvivalTimeFirstLevel = 10 * 1000L
    private val stackActivitySurvivalTimeIncremental = 5 * 1000L
    private val taskSurvivalTime = 10 * 60 * 1000L

    private var activityManager: ActivityManager? = null
    private lateinit var activityQueue: ArrayList<Activity>
    private lateinit var survivalTimeMap: SparseArray<Long>

    private var currentActivity: Activity? = null
    private var currentActivitySurvivalTime = 0L
    private lateinit var taskSurvivalTimeMap: SparseArray<Long>

    private lateinit var handler: Handler

    override fun callApplicationOnCreate(app: Application?) {
        super.callApplicationOnCreate(app)

        activityQueue = ArrayList()
        survivalTimeMap = SparseArray()
        taskSurvivalTimeMap = SparseArray()

        handler = Handler()

        Log.e(TAG, "callApplicationOnCreate")

        postCheck()
    }

    override fun callActivityOnCreate(activity: Activity?, icicle: Bundle?) {
        super.callActivityOnCreate(activity, icicle)

        activity?.let {
            val now = System.currentTimeMillis()
            putSurvivalTime(it, now)
            putTaskSurvivalTime(it, now)
        }
    }

//    override fun callActivityOnStart(activity: Activity?) {
//        super.callActivityOnStart(activity)
//    }

    override fun callActivityOnResume(activity: Activity?) {
        super.callActivityOnResume(activity)

        activity?.let {
            currentActivity = it
            currentActivitySurvivalTime = System.currentTimeMillis()
        }
    }

//    override fun callActivityOnPause(activity: Activity?) {
//        super.callActivityOnPause(activity)
//    }

    override fun callActivityOnDestroy(activity: Activity?) {
        super.callActivityOnDestroy(activity)

        activity?.let {
            val index = activityQueue.indexOf(it)
            if (index >= 0) {
                // exist
                removeActivity(index)
            }
        }
    }

    private fun removeActivity(index: Int) {
        activityQueue.removeAt(index)
        survivalTimeMap.remove(index)
    }

    private fun putSurvivalTime(activity: Activity, now: Long) {
        val index = activityQueue.size
        activityQueue.add(activity)
        survivalTimeMap.put(index, now)
    }

    private fun putTaskSurvivalTime(activity: Activity, now:Long) {
        val taskId = activity.taskId
        if (taskSurvivalTimeMap.get(taskId, 0L) == 0L) {
            taskSurvivalTimeMap.put(taskId, now)
        }
    }

    private fun postCheck() {
        handler.postDelayed({
            checkActivityStatus()
        }, checkTaskInterval)
    }

    /**
     * check activity status
     */
    private fun checkActivityStatus() {
        checkTopActivity()
        checkStack()
        checkTaskActivity()
    }

    private fun checkTopActivity() {
        if (currentActivity != null) {
            if (System.currentTimeMillis() - currentActivitySurvivalTime > topActivitySurvivalTime) {
                // exceeding maximum survival time
                currentActivity?.finish()
                currentActivity = null
                currentActivitySurvivalTime = 0L
            }
        }
    }

    private fun checkStack() {
        if (activityManager == null) {
            if (context != null) {
                activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
            }
        }

        if (activityManager != null) {
            val now = System.currentTimeMillis()
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
                activityManager?.let {
                    val appTaskList = it.appTasks
                    if (!appTaskList.isEmpty()) {
                        val appTask = appTaskList[0]
                        val taskId = appTask.taskInfo.id
                        val taskTime = taskSurvivalTimeMap.get(taskId)
                        if (taskTime != null && now - taskTime > taskSurvivalTime) {
                            activityQueue.reversed()
                                    .forEach {
                                        if (it.taskId == taskId) {
                                            val index = activityQueue.indexOf(it)
                                            removeActivity(index)
                                        }
                                    }
                            appTask.finishAndRemoveTask()
                        }
                    }
                }
            } else {
                val runningTaskInfoList = activityManager?.getRunningTasks(1)
                runningTaskInfoList?.let {
                    if (!it.isEmpty()) {
                        val runningTaskInfo = it[0]
                        val taskId = runningTaskInfo.id
                        val taskTime = taskSurvivalTimeMap.get(taskId)
                        if (taskTime != null && now - taskTime > taskSurvivalTime) {
                            activityQueue.reversed()
                                    .forEach {
                                        if (it.taskId == taskId) {
                                            val index = activityQueue.indexOf(it)
                                            removeActivity(index)
                                            it.finish()
                                        }
                                    }
                        }
                    }

                }
            }
        }
    }

    private fun checkTaskActivity() {
        var time = stackActivitySurvivalTimeFirstLevel
        val now = System.currentTimeMillis()
        var needClearActivity: Activity? = null

        var index = activityQueue.size - 1
        while (index > 0) {
            if (now - survivalTimeMap.get(index, 0L) > time) {
                needClearActivity = activityQueue[index]
                break
            }
            time += stackActivitySurvivalTimeIncremental
            index--
        }

        needClearActivity?.let {
            val id = it.taskId
            activityQueue.reversed()
                    .forEach {
                        if (it.taskId == id) {
                            val i = activityQueue.indexOf(it)
                            removeActivity(i)
                            it.finish()
                        }
                    }
        }
    }
}