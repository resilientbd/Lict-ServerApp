public class BatchData {

    String batch_name ;
    String uni_name ;
    String trainer_name ;
    String start ;
    String end ;
    String day ;
    String date ;

    public BatchData() {
    }

    public BatchData(String batch_name, String uni_name, String trainer_name, String start, String end, String day, String date) {
        this.batch_name = batch_name;
        this.uni_name = uni_name;
        this.trainer_name = trainer_name;
        this.start = start;
        this.end = end;
        this.day = day;
        this.date = date;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public String getUni_name() {
        return uni_name;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }
}
