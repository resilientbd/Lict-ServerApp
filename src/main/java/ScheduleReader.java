import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ScheduleReader {


//    public static final String SAMPLE_XLSX_FILE_PATH = "BHT.xlsx";

    public ScheduleReader(Firestore db) throws IOException, InvalidFormatException, InterruptedException, ExecutionException {

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath().toString();
//            System.out.println(selectedFile.getAbsolutePath());
//            InputStream serviceAccount = new FileInputStream("lict-6c37e-firebase-adminsdk-y0zlq-50d30c1ade.json");
//            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(credentials)
//                    .build();
//            FirebaseApp.initializeApp(options);
//
//            Firestore db = FirestoreClient.getFirestore();

//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Workbook workbook = WorkbookFactory.create(new File(filePath).getAbsoluteFile());

            System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            System.out.println("Retrieving Sheets using Iterator");
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                System.out.println("=> " + sheet.getSheetName());
            }

            System.out.println("Retrieving Sheets using for-each loop");
            for(Sheet sheet: workbook) {
                System.out.println("=> " + sheet.getSheetName());
            }

            System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
            workbook.forEach(sheet -> {
                System.out.println("=> " + sheet.getSheetName());
            });

        /*
           ==================================================================
           Iterating over all the rows and columns in a Sheet (Multiple ways)
           ==================================================================
        */

            // Getting the Sheet at index zero
            Sheet sheet = workbook.getSheetAt(0);

            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();

            System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
            Iterator<Row> rowIterator = sheet.rowIterator();
            int count =1;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(count != 1){

//            count++;
                    Iterator<Cell> cellIterator = row.cellIterator();
                    ArrayList<String> data = new ArrayList<>();

                    while (cellIterator.hasNext() ) {

                        Cell cell = cellIterator.next();
                        int idx = cell.getColumnIndex();
                        String cellValue = dataFormatter.formatCellValue(cell);

                        data.add(cellValue.toString());
                    }
                    if(data.size() == 8){
                        System.out.println("asd" + data.size());
                        CollectionReference docRef = db.collection("batch_status");
                        System.out.println(docRef.getId());
                        // Add document data  with id "alovelace" using a hashmap
                        Map<String, Object> datas = new HashMap<>();
                        datas.put("batch_code", data.get(0));
                        datas.put("trainer_name", data.get(1));
                        datas.put("university_name", data.get(2));
                        datas.put("date", data.get(3));
                        datas.put("day", data.get(4));

                        String start, end;

                        if(data.get(5) != ""){
                            start = data.get(5);
                            start = new StringBuilder(start).insert(start.length()-2, ":").toString();
                        }
                        else{
                            start = "";
                        }

                        if(data.get(6) != ""){
                            end = data.get(6);
                            end = new StringBuilder(end).insert(end.length()-2, ":").toString();
                        }
                        else{
                            end = "";
                        }

                        datas.put("start", start);

                        datas.put("end", end);
                        datas.put("status", data.get(7));
                        datas.put("attendance", "");
                        datas.put("intime", "");
                        datas.put("outtime", "");

                        //asynchronously write data
                        ApiFuture<DocumentReference> result = docRef.add(datas);
                        // ...
                        // result.get() blocks on response
//                System.out.println("Update time : " + result.get().getUpdateTime());

                        data.clear();
                    }
                }

                count++;
            }

            Sheet sheet2 = workbook.getSheetAt(1);

            DataFormatter dataFormatter2 = new DataFormatter();

            System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
            Iterator<Row> rowIterator2 = sheet2.rowIterator();
            while (rowIterator2.hasNext()) {
                Row row = rowIterator2.next();

                Iterator<Cell> cellIterator = row.cellIterator();
                ArrayList<String> data = new ArrayList<>();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    int idx = cell.getColumnIndex();
                    String cellValue = dataFormatter2.formatCellValue(cell);
                    data.add(cellValue.toString());
                }

                DocumentReference docRef3 = db.collection("trainer_details").document(data.get(1));
                // Add document data  with id "alovelace" using a hashmap
                Map<String, Object> datas = new HashMap<>();
                datas.put("name", data.get(1));
                datas.put("mobile", data.get(2));
                datas.put("email", data.get(3));

                //asynchronously write data
                ApiFuture<WriteResult> result3 = docRef3.set(datas);

                // result.get() blocks on response
                System.out.println("Update time : " + result3.get().getUpdateTime());

                data.clear();
            }

            // Getting the Sheet at index zero
            Sheet sheet3 = workbook.getSheetAt(2);

            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter3 = new DataFormatter();


            System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
            Iterator<Row> rowIterator3 = sheet3.rowIterator();
            while (rowIterator3.hasNext()){
                Row row = rowIterator3.next();


                Iterator<Cell> cellIterator = row.cellIterator();
                ArrayList<String> data = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int idx = cell.getColumnIndex();
                    String cellValue = dataFormatter3.formatCellValue(cell);
                    data.add(cellValue.toString());
                }

                DocumentReference docRef2 = db.collection("university_details").document(data.get(0));

                System.out.println("Enter Uni:");
                Map<String, Object> datas2 = new HashMap<>();
                datas2.put("address", data.get(2));
                datas2.put("lat_long", data.get(3));
                datas2.put("location", data.get(1));
                datas2.put("university_name", data.get(0));
                ApiFuture<WriteResult> result2 = docRef2.set(datas2);

                System.out.println("Update time : " + result2.get().getUpdateTime());

                data.clear();
            }
        }
    }
}
