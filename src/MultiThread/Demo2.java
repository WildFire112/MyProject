package MultiThread;

class Demo2 implements Runnable {
    public void run() {
        try {
            System.out.println("Thread " +
                    Thread.currentThread().getId() +
                    " is running");

            for(int i = 0; i < 10; i ++){
                Thread.sleep(2000);
                System.out.println(2);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}