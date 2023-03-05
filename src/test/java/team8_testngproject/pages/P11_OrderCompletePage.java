package team8_testngproject.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import team8_testngproject.utilities.Driver;

public class P11_OrderCompletePage {
    public P11_OrderCompletePage() {
        PageFactory.initElements(Driver.getDriver(),this);
    }







    // 20.Satır --> Ali





























    // 50.Satır --> Esra





























    // 80.Satır --> Gonca





























    // 110.Satır --> Gürkay





























    // 140.Satır --> Hüseyin





























    // 170.Satır --> Mehmet





























    // 200.Satır --> Meryem





























    // 230.Satır --> Zeliha





























    // 260.Satır --> Nurullah





























    // 290.Satır --> Ramazan





























    // 320.Satır --> Sevda





























    // 350.Satır --> Mustafa

    @FindBy (xpath = "//*[text()='Thank you. Your order has been received.']")
    public WebElement shoppingAndText;

    public void checkShoopingEnd(){
        Assert.assertEquals(shoppingAndText.getText(), "Thank you. Your order has been received.");
    }




























}