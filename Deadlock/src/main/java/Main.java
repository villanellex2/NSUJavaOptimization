public class Main {

    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();
        lockedThread thread = new lockedThread(lock1, lock2);
        lockedThread thread2 = new lockedThread(lock2, lock1);
        thread.start();
        thread2.start();
        try {
            thread.join();
            thread2.join();
        }
        catch (InterruptedException ignored){}
    }

    private static class lockedThread extends Thread {
        final Object obj1;
        final Object obj2;

        lockedThread(Object obj1, Object obj2) {
            this.obj1 = obj1;
            this.obj2 = obj2;
        }

        public void run() {
            synchronized (obj1) {
                System.out.println(this + " get first obj " + obj1);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }

                synchronized (obj2) {
                    System.out.println(this + " get second obj " + obj2);
                }
            }
        }
    }
}