package testdata;

/**
 * Immutable test-data holder for the Student Details form, built via {@link Builder}
 * so form values live in one place instead of scattered inline strings.
 */
public final class StudentTestData {

    private final String fullName;
    private final String fatherName;
    private final String motherMaidenName;
    private final String studentId;
    private final String dob;
    private final String email;
    private final String phone;
    private final String course;
    private final String year;
    private final String address;

    private StudentTestData(Builder builder) {
        this.fullName = builder.fullName;
        this.fatherName = builder.fatherName;
        this.motherMaidenName = builder.motherMaidenName;
        this.studentId = builder.studentId;
        this.dob = builder.dob;
        this.email = builder.email;
        this.phone = builder.phone;
        this.course = builder.course;
        this.year = builder.year;
        this.address = builder.address;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCourse() {
        return course;
    }

    public String getYear() {
        return year;
    }

    /** Optional field - may be null/empty; the form's Address textarea is not required. */
    public String getAddress() {
        return address;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Default valid data set used by the positive login + submission scenario. */
    public static StudentTestData defaultValidStudent() {
        return StudentTestData.builder()
                .fullName("Vibhav Kulshrestha")
                .fatherName("Anil Kulshrestha")
                .motherMaidenName("Sunita Verma")
                .studentId("STU2026001")
                .dob("2003-05-14")
                .email("vibhav.kul@example.com")
                .phone("9876543210")
                .course("Computer Science")
                .year("3rd")
                .address("12 MG Road, Agra, Uttar Pradesh")
                .build();
    }

    public static final class Builder {
        private String fullName;
        private String fatherName;
        private String motherMaidenName;
        private String studentId;
        private String dob;
        private String email;
        private String phone;
        private String course;
        private String year;
        private String address;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder fatherName(String fatherName) {
            this.fatherName = fatherName;
            return this;
        }

        public Builder motherMaidenName(String motherMaidenName) {
            this.motherMaidenName = motherMaidenName;
            return this;
        }

        public Builder studentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder dob(String dob) {
            this.dob = dob;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder course(String course) {
            this.course = course;
            return this;
        }

        public Builder year(String year) {
            this.year = year;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public StudentTestData build() {
            return new StudentTestData(this);
        }
    }
}
