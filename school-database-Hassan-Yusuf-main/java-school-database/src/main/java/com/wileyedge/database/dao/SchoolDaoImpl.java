package com.wileyedge.database.dao;

import com.wileyedge.database.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class SchoolDaoImpl implements SchoolDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SchoolDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    * Add each SQL statement to the methods below.
    * The SQL statement must be completely inside the quotation marks provided
    * in the existing Java statement:
    *   String sql = "";
    *
    * Special notes:
    *   - Strings must be inside single quotation marks (' ').
    *   - Strings are case-sensitive.
    *   - Semi-colons are optional at the end of the SQL statement.
    *
    * Do not change any code outside of the placeholders provided.
     */

    @Override
    public List<Student> allStudents() {
        // Write a query that returns all students (first name, last name only)
        // sorted by last name.
        // YOUR CODE STARTS HERE

        String sql = "SELECT fName, lName FROM STUDENT ORDER BY lName;";

        // YOUR CODE ENDS HERE

        return jdbcTemplate.query(sql, new StudentMapper());
    }

    @Override
    public List<Course> csCourses() {
        // Write a query that lists the course code and course name
        // for all courses in the Computer Science department.
        // YOUR CODE STARTS HERE
        //Selects course code and course description where a coursecode record begins with 'CS'
         String sql = "SELECT courseCode, courseDesc FROM COURSE WHERE courseCode LIKE 'CS%';";

        // YOUR CODE ENDS HERE
        return jdbcTemplate.query(sql, new CourseMapper());
    }

    @Override
    public List<TeacherCount> teacherCountByDept() {
        //  Write a query that displays the department and the total number of teachers assigned to each department.
        //  Name the aggregate field `teacherCount`.
        // YOUR CODE STARTS HERE
        //First group by dept, then count the number of teachers with the same dept as 'teacherCount' and then retrieves dept
        String sql = "SELECT dept, count(tid) as teacherCount FROM TEACHER GROUP BY dept;";

        // YOUR CODE ENDS HERE
        return jdbcTemplate.query(sql, new TeacherCountMapper());
    }

    @Override
    public List<StudentClassCount> studentsPerClass() {
        // Write a query that lists the course code and course description for each course,
        // with the number of students enrolled in each course.
        // Name the aggregate field `numStudents`.
        // YOUR CODE STARTS HERE

        //This will first join the course_student table with the course table, then group by course_id, then count the number of students
        //with the same course_id as numStudents, then retrieve the courseCode and courseDesc from course

        String sql = "SELECT courseCode, courseDesc, count(course_id) as numStudents FROM course_student" +
                " INNER JOIN course ON course_student.course_id = course.cid GROUP BY course_id;";

        // YOUR CODE ENDS HERE
        return jdbcTemplate.query(sql, new StudentCountMapper());
    }

    // This step includes two parts. Both parts must be completed to pass the test.
    // Create a new student and enroll the new student in a course
     @Override
    public void addStudent() {
        // Part 1: Write a query to add the student Robert Dylan to the student table.
        // YOUR CODE STARTS HERE
        //Inserts Robert Dylan into the student table
        String sql = "INSERT INTO Student (fName,lName) VALUES ('Robert', 'Dylan');";


        // YOUR CODE ENDS HERE
         System.out.println(jdbcTemplate.update(sql));

    }

    @Override
    public void addStudentToClass() {
        // Part 2: Write a query to add Robert Dylan to CS148.
        // YOUR CODE STARTS HERE
        //Uses a subquery to insert Robert Dylan into the course_student table where the course code is CS148 (also uses a subquery to find the cid from course where courseCode is CS148)
        String sql = "INSERT INTO course_student (student_id, course_id) VALUES ((SELECT sid FROM Student" +
                " WHERE fName = 'Robert' AND lName = 'Dylan'), (SELECT cid FROM course WHERE courseCode = 'CS148'));";

        // YOUR CODE ENDS HERE
        jdbcTemplate.update(sql);
    }

    @Override
    public void editCourseDescription() {
        // Write a query to change the course description for course CS305 to "Advanced Python with Flask".
        // YOUR CODE STARTS HERE
        //updates courseDesc where courseCode is CS305
        String sql = "UPDATE course SET courseDesc = 'Advanced Python with Flask' WHERE courseCode = 'CS305';";

        // YOUR CODE ENDS HERE
        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteTeacher() {
        // Write a query to remove David Mitchell as a teacher.
        // YOUR CODE STARTS HERE
        // Removes David Mitchell from the teacher table
        String sql = "DELETE FROM teacher WHERE tFName = 'David' AND tLName = 'Mitchell';";
        // YOUR CODE ENDS HERE
        jdbcTemplate.update(sql);
    }

    //***** EXTRA HELPER METHODS
    //***** DO NOT CHANGE THE SQL STRING IN THESE METHODS!!!
    @Override
    public List<Teacher> listAllTeachers() {
        String sql = "Select * from Teacher;";
        return jdbcTemplate.query(sql, new TeacherMapper());
    }

    @Override
    public List<Student> studentsCS148() {
        String sql = "select fname, lname\n" +
                "from student s \n" +
                "join course_student cs on s.sid = cs.student_id\n" +
                "where course_id = 1;";
        return jdbcTemplate.query(sql, new StudentMapper());
    }
}
