import com.google.cloud.firestore.Firestore;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class HelloJava {

    public static void main(String[] args)
    {
       // System.out.println("Hello");

        MyForm form= null;
        try {
            form = new MyForm();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        form.setVisible(true);
    }
}
