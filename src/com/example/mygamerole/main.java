package com.example.mygamerole;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new mySurface(this));
	}

}

class mySurface extends SurfaceView implements Callback, Runnable {

	private static final String tag = null;
	private SurfaceHolder sfh;
	private Bitmap bmp;
	private int bmpX, bmpY;
	private Canvas c;
	private Thread th;
	private boolean flag;
	private final int DIR_LEFT = 0;
	private final int DIR_RIGHT = 1;
	private int dir = DIR_RIGHT;
	private int currentFrame = 0;

	public mySurface(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		sfh = this.getHolder();
		sfh.addCallback(this);

	}

	public void myDraw() {
		try {
			c = sfh.lockCanvas();
			if (c != null) {
				drawFrame(currentFrame, c);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (c != null) {
				sfh.unlockCanvasAndPost(c);
			}
		}
	}

	public void drawFrame(int currentFrame, Canvas canvas) {
		Log.i(tag, "-----frame=" + currentFrame);
		int frameW = bmp.getWidth() / 6;
		int frameH = bmp.getHeight() / 2;
		int col = bmp.getWidth() / frameW;
		int x = currentFrame % col * frameW;
		int y = currentFrame / col * frameH;

		canvas.drawRGB(255, 255, 255);
		canvas.save();
		canvas.clipRect(bmpX, bmpY, bmpX + bmp.getWidth() / 6,
				bmpY + bmp.getHeight() / 2);
		if (dir == DIR_LEFT) {
			canvas.scale(-1, 1, bmpX - x + bmp.getWidth() / 2,
					bmpY - y + bmp.getHeight() / 2);
		}
		canvas.drawBitmap(bmp, bmpX - x, bmpY - y, null);
		canvas.restore();
	}

	public void logic() {
		/*
		 * if (bmpX > 0) { bmpX = this.getWidth() - bmp.getWidth(); } else bmpX
		 * += bmpX;
		 */
		currentFrame++;
		if (currentFrame > 11)
			currentFrame = 0;
		if ((bmpX + bmp.getWidth() / 6) > this.getRight())
			dir = DIR_LEFT;
		if (dir == DIR_LEFT && bmpX < 0)
			dir = DIR_RIGHT;
		if (dir == DIR_RIGHT)
			bmpX += 1;
		else
			bmpX -= 1;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 33) {
					Thread.sleep(33 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.robot);
		bmpX = 0;
		bmpY = 0;
		flag = true;
		th = new Thread(this);
		th.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		flag = false;
	}

}