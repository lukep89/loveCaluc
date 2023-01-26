package sg.edu.nus.iss.app.loveCalcu.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Random;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class LoveResult implements Serializable {

    public static Object getPercentage;
    private String fname;
    private String sname;
    private String percentage;
    private String result;

    private String id;

    LoveResult() {
        this.id = genrateId(8);
    }

    public LoveResult(String fname, String sname) {
        this.fname = fname;
        this.sname = sname;

    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private synchronized String genrateId(int numOfChar) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();

        while (sb.length() < numOfChar) {
            sb.append(Integer.toHexString(rnd.nextInt()));

        }
        return sb.toString().substring(0, numOfChar);
    }

    // from a Json string create a java object (LoveResult model)
    public static LoveResult create(String json) throws IOException {
        LoveResult lrObj = new LoveResult();

        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();

            // remove encoding chars from API
            String person1Name = URLDecoder.decode(o.getString("fname"), "UTF-8");
            String person2Name = URLDecoder.decode(o.getString("sname"), "UTF-8");

            lrObj.setFname(person1Name);
            lrObj.setSname(person2Name);
            lrObj.setPercentage(o.getString("percentage"));
            lrObj.setResult(o.getString("result"));
        }

        return lrObj;
    }

    // // to create a Json object from the java object (LoveResult model). the
    // // ResponseEntity.body() remember toString before save to db.
    // public JsonObject toJSON() {
    // return Json.createObjectBuilder()
    // .add("fname", this.getFname())
    // .add("sname", this.getSname())
    // .add("percentage", this.getPercentage())
    // .add("result", this.getResult())
    // // .add("id", this.getId())
    // .build();
    // }

}
