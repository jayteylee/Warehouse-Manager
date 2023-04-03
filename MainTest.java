package cosc201.a2;

public class MainTest {
    public static void main(String[] args) {

        double totalEfficiency = 0;
        double averageEfficiency;
        int numTests = 100;

        for(int i = 0; i < numTests; i++){
        TestWarehouse b = new TestWarehouse();
        totalEfficiency += b.run();
        }

        averageEfficiency = totalEfficiency/numTests;

        System.out.println("Average Efficiency = " + averageEfficiency);
    }
}
