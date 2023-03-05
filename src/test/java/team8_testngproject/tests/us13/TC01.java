package team8_testngproject.tests.us13;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import team8_testngproject.pages.P01_HomePage;
import team8_testngproject.pages.P03_LoginPage;
import team8_testngproject.pages.P04_MyAccountPage;
import team8_testngproject.pages.P05_AddressesPage;
import team8_testngproject.utilities.ConfigReader;
import team8_testngproject.utilities.Driver;
import team8_testngproject.utilities.RaporlamaUtil;
import team8_testngproject.utilities.ReusableMethods;

public class TC01 {
    private final String testName = "US13 || TC01-Shipping Adress POSITIVE senaryo";
    private final String description = "Vendor Shipping Address kaydı tamamlanmalı";
    private final String raporMesaji = "Vendor Shipping Adress kaydi tamamlanmistir.";


    @Test(testName = testName, description = "<span style='font-weight:bold'>Amaç:</span> " + description)
    public void tc01() {
        //     1. Belirtilen URL'e gidilir.
        Driver.getDriver().get(ConfigReader.getProperty("URL"));

        //     2. Sing in butonuna tıklanir.
        P01_HomePage homePage = new P01_HomePage();
        homePage.signInButtonAli.click();

        //     3. Username ya da Email inputuna veri girilir.
        P03_LoginPage loginPage = new P03_LoginPage();
        loginPage.usernameAli.sendKeys(ConfigReader.getProperty("vendorUserNameAli"));

        //     4.  Password inputuna veri girilir.
        loginPage.passwordAli.sendKeys(ConfigReader.getProperty("vendorPasswordAli"));

        //      5. signOut butonuna tıklar
        loginPage.loginAli.click();
        RaporlamaUtil.extentTestInfo("Login islemi yapıldı");

        //      6. signOut butonuna tıklar
        homePage.signOutButtonAli.click();

        //      7. My Account sayfasına erişildiğini dogrulanir.
        P04_MyAccountPage myAccountPage = new P04_MyAccountPage();
        Assert.assertEquals(myAccountPage.myAccountPageAli.getText(), "My Account");
        RaporlamaUtil.extentTestInfo("MyAccount sayfasında oldugu dogrulandı.");

        //     8. Address butonuna tıklar.
        myAccountPage.AddressesLinkAli.click();

        //     9. Shipping (sipariş) addressi add (edit your billing) butonuna tıklar
        P05_AddressesPage addressesPageAli = new P05_AddressesPage();
        ReusableMethods.jsClick(addressesPageAli.shippingaddButtonAli);

        //     10.  First name inputuna veri girilir. //Last name inputuna veri girilir.
        addressesPageAli.shippingfirstNameAli.clear();
        addressesPageAli.shippinglastNameAli.clear();
        addressesPageAli.shippingfirstNameAli.sendKeys("Preto",
                Keys.TAB, "MEX");

        //    11.  Country/Region dropdown'indan secim yapilir.
        Select select = new Select(addressesPageAli.shippingcountryAli);
        select.selectByVisibleText("Turkey");

        //     12. Street adress alanina veri girilir.
        addressesPageAli.shippingstreetAli.clear();
        addressesPageAli.shippingstreetAli.sendKeys(" Mecburiyet Street");

        //     13.  Postcode / ZIP alanina veri girilir.
        addressesPageAli.shippingpostcodeAli.clear();
        addressesPageAli.shippingpostcodeAli.sendKeys("123456");

        //     14.  Town/City alanina veri girilir.
        addressesPageAli.shippingcityAli.sendKeys("Elbistan");

        //     15.  Provience dropdown'undan secim yapilir.
        Select selectProvience = new Select (addressesPageAli.shippingstateAli);
        selectProvience.selectByIndex(5);

        //     16.  SAVE ADDRESS butonuna tiklanir.
        ReusableMethods.jsClick(addressesPageAli.saveAdressShippingButtonAli);

        //     19. Kaydedilen adresin shipping Address olarak kayit edildigi dogrulanir
       Assert.assertTrue(addressesPageAli.shippingaddreschangedsuccessfully.getText().contains("Address changed successfully."));
        RaporlamaUtil.extentTestInfo("degişiklik shipping adrese eklendi.");

      Driver.closeDriver();
      RaporlamaUtil.message = "<span style='color:green; font-weight:bold; font-size: 14px'>TEST SONUCU: </span><br><span style='color:purple; font-size: 16px'>" + raporMesaji + "</span>";


    }
}
