package com.gravity.innovations.tasks.done;

import android.view.View;

public class DemoHelper {

	public static interface StartDemo {

		void startInstructionsDemo(View view);

		void navToggleInstructions(View view);

		void navToggleDemo(View view);

		void newCatagoryInstructionsDemo(final CustomIconListAdapter adapter);

		void newCatagoryDemo();
	}

	public static interface TaskListDemo {
		void demo1_delBtn();

		void demo2_editBtn();

		void demo3_shareBtn();

		void demo4_sharedWithBtn();

		void demo5_listTitle();

		void demo6_navToggle();

		void demo7_syncImage();

		void demo8_fabBtn();

		void demo9_addAnotherTask();

		void demo10_openNavDrawer();
		// void taskActivityDemo_taskTitle();

		// void taskActivityDemo_taskDiscardandSave();

	}

	public static interface TaskOperations {
		void demoTaskOp1_openDetails();

		void demoTaskOp2_taskLongClick();

		void demoTaskOp3_markDoneInstructions();

		void demoTaskOp4_markDone();
	}

	public static interface TaskActivityDemo {

		void demo1_title();

		void demo2_details();

		void demo3_allDay();

		void demo4_startDateTime();

		void demo5_endDateTime();

		void demo6_repeat();

		void demo7_notification();

		void demo8_notes();

	}

	public static interface DashBoardDemo {
		void demo1_openDashInstructions();

		void demo2_fragDashInstruction();

		void demo3_fragDashSearch();

		void demo4_fragDashSearch();

		void demo5_fragDashEnd();
	}
}