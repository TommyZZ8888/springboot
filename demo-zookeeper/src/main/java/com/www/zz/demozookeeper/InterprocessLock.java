package com.www.zz.demozookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Describtion: InterprocessLock
 * @Author: 张卫刚
 * @Date: 2025/5/31 17:50
 */
public class InterprocessLock {
	public static void main(String[] args)  {
		CuratorFramework zkClient = getZkClient();
		String lockPath = "/lock";
		InterProcessMutex lock = new InterProcessMutex(zkClient, lockPath);
		//模拟50个线程抢锁
		for (int i = 0; i < 50; i++) {
			new Thread(new TestThread(i, lock)).start();
		}
	}


	static class TestThread implements Runnable {
		private Integer threadFlag;
		private InterProcessMutex lock;

		public TestThread(Integer threadFlag, InterProcessMutex lock) {
			this.threadFlag = threadFlag;
			this.lock = lock;
		}

		@Override
		public void run() {
			try {
				lock.acquire();
				System.out.println("第"+threadFlag+"线程获取到了锁");
				//等到1秒后释放锁
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					lock.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static CuratorFramework getZkClient() {
		String zkServerAddress = "127.0.0.1:2185";
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000);
		CuratorFramework zkClient = CuratorFrameworkFactory.builder()
				.connectString(zkServerAddress)
				.sessionTimeoutMs(5000)
				.connectionTimeoutMs(5000)
				.retryPolicy(retryPolicy)
				.build();
		zkClient.start();
		return zkClient;
	}
}