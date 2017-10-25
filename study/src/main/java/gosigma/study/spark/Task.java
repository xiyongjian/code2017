package gosigma.study.spark;

import java.io.Serializable;

public abstract class Task implements Serializable {
    public void run() {
        System.out.println("run task!");
    }
}
