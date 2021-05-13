public class Main {

    static final CountDownLatch latch = new CountDownLatch(2);
    static final Object lock = new Object();

    public static void main(String[] args) {
        Thread thread1 = new DThread();
        Thread thread2 = new DThread();
        thread1.start();
        thread2.start();
    }

    private static class DThread extends Thread {

        @Override
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread().toString() +  "get a lock");
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private static class CountDownLatch {
        private int count;

        public CountDownLatch(int count) {
            this.count = count;
        }

        public void await() throws InterruptedException {
            synchronized (this) {
                System.out.println(Thread.currentThread().toString() +  "await");
                if (count > 0) {
                    this.wait();
                }
            }
        }

        public void countDown() {
            synchronized (this) {
                count--;
                if (count == 0) {
                    this.notifyAll();
                }
            }
        }
    }
}