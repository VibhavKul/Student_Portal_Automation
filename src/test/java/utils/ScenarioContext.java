package utils;

import testdata.StudentTestData;

/**
 * Thread-local scratch space for passing data between step definition classes
 * within a single scenario (Cucumber's default object factory does not share
 * instance state across glue classes).
 */
public final class ScenarioContext {

    private static final ThreadLocal<StudentTestData> submittedStudentData = new ThreadLocal<>();

    private ScenarioContext() {
    }

    public static void setSubmittedStudentData(StudentTestData data) {
        submittedStudentData.set(data);
    }

    public static StudentTestData getSubmittedStudentData() {
        return submittedStudentData.get();
    }

    public static void clear() {
        submittedStudentData.remove();
    }
}
