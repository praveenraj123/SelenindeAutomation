package utils;

import java.time.Duration;

public class SleepingUtils {

  public static void Wait(Duration time){
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
