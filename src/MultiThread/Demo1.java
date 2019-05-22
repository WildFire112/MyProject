package MultiThread;

class Demo1 extends Thread {
    public void run() {
        try {
            System.out.println("Thread " +
                    Thread.currentThread().getId() +
                    " is running");

            for(int i = 0; i < 10; i ++){
                Thread.sleep(1000);
                System.out.println(1);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

