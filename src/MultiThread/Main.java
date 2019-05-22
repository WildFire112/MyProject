package MultiThread;

class Main {

    public static void main(String[] args){
        Demo1 obj1 = new Demo1();
        Thread obj2 = new Thread(new Demo2());

        obj1.start();
        obj2.start();
    }

}
