/*  *************************************************************
    *                                                           *
    *   Take out setDate and setTime methods to Facade classes  *
    *                                                           *
    *************************************************************
*/

package pageComponents.menu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Date;

public class OrderConfirmationPageComponent {
    private final WebDriver driver;
    private WebDriverWait wait;

    private Duration duration;

    public OrderSummaryPageComponent orderSummary;

    public OrderConfirmationPageComponent(WebDriver driver)  {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        orderSummary = new OrderSummaryPageComponent(driver);
    }
    @FindBy(xpath = "//label[contains(., 'Date picker')]/..//*[@type='text']")
    private WebElement datePickerField;

    @FindBy(xpath = "//label[contains(., 'Time picker')]/..//*[@type='text']")
    private WebElement timePickerField;

    @FindBy(xpath = "//button/span[text()='Submit']")
    private WebElement submitBtn;

    @FindBy(xpath = "//button/span[text()='Cancel']/ancestor::button")
    private WebElement cancelBtn;


    @FindBy(xpath = "//h6[contains(., 'Order confirmation')]")
    private WebElement orderConfirmationField;

    public OrderConfirmationPageComponent setDate(Date desiredDate) {
        DatePickerPageComponent datePicker = new DatePickerPageComponent(driver, desiredDate);
        datePickerField.click();
        datePicker.setDesiredYear();
        datePicker.setDesiredMonth();
        datePicker.setDesiredDay();
        return datePicker.confirmDate();
    }

    public OrderConfirmationPageComponent setTime(Date desiredDate) {
        TimePickerPageComponent timePicker = new TimePickerPageComponent(driver, desiredDate);
        timePickerField.click();
        timePicker.setAmPm();
        timePicker.setHours();
        timePicker.setMinutes();
        return timePicker.confirmTime();
    }

    public String getTime() {
        return timePickerField.getAttribute("value");
    }

    public String getDate() {
        return datePickerField.getAttribute("value");
    }

    public void cancelOrder() {
        cancelBtn.click();
    }

    public void cancelOrder(Duration duration) {
        wait = new WebDriverWait(driver, duration);
        for (int i = 0; i <= 2; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(By
                        .xpath("//button/span[text()='Cancel']/ancestor::button")));
                break;
            } catch (Exception e) {

            }
        }
        cancelBtn.click();
    }

    public void submitOrder(Duration duration) {
        wait = new WebDriverWait(driver, duration);
        for (int i = 0; i <= 2; i++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(By
                        .xpath("//button/span[text()='Submit']")));
                break;
            } catch (Exception e) {

            }
        }
        submitBtn.click();
    }

    public void submitOrder() {
        submitBtn.click();
    }

    public String orderConfirmationFieldVisible() {
        return orderConfirmationField.getText();
    }
}
