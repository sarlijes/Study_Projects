
public class Line {

    private String edulit_id, indicator, gender, location, country, flagCode, flag, year, value;
    private int intYear;
    private double doubleValue;

    public Line(String edulit_id, String indicator, String gender, String location, String country, String year, String value) {
        this.edulit_id = edulit_id;
        this.indicator = indicator;
        this.gender = gender;
        this.location = location;
        this.country = country;
        this.flagCode = flagCode;
        this.flag = flag;
        if (year.isEmpty()) {
            this.intYear = -1;
        } else {
            this.intYear = Integer.valueOf(year);
        }
        if (value.isEmpty()) {
            this.doubleValue = -1.0;
        } else {
            this.doubleValue = Double.valueOf(value);
        }
    }

    public String getLocation() {
        return location;
    }

    public double getValue() {
        return doubleValue;
    }

    public int getYear() {
        return intYear;
    }

    @Override
    public String toString() {
        return "{" + indicator + ", " + country + ", " + gender + ", " + intYear + ": value: " + doubleValue + '}';
    }
}
