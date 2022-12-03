package net.absoft;
import org.testng.annotations.BeforeMethod;

public class BaseTestPositive {
    @BeforeMethod(
            groups = "Positive"
    )
    public void baseSetUp() {
        System.out.println("Base Setup Positive");
    }

}
