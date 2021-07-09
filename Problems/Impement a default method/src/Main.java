// do not change code below
class Main {
    public static void main(String... args) {
        Printer printer = new ConsolePrinter();
        printer.print(); // prints: This is a default message
    }
}

class ConsolePrinter implements Printer {
}

interface Printer {
    default void print(){
        System.out.print("This is a default message");
    }
}