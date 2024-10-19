package org.example;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class MainTest {

  public static void main(String[] args) {

    Selenide.open("https://selenide.org/documentation.html");
    $(By.partialLinkText("Selenide javadoc."));
    
  }

}
