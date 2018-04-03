package tk.vn.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by neelp on 28-03-2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompactCourseBean {

    private String courseName;
    private long courseId;
    private String info;

    public CompactCourseBean(){}

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}